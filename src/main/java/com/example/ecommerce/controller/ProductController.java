package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.dto.ProductResponse;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 * 處理商品的管理和查詢API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理", description = "商品的增刪改查API")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 獲取所有商品（分頁）
     * @param page 頁碼（從0開始）
     * @param size 每頁大小
     * @param sortBy 排序欄位
     * @param direction 排序方向（asc或desc）
     * @return 商品分頁結果
     */
    @GetMapping
    @Operation(summary = "獲取所有商品", description = "分頁獲取所有商品的列表")
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        
        // 創建分頁和排序參數
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        // 查詢商品
        Page<Product> productPage = productRepository.findAll(pageable);
        
        // 將Product實體轉換為ProductResponse DTO
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productResponses);
    }

    /**
     * 根據分類獲取商品（分頁）
     * @param categoryId 分類ID
     * @param page 頁碼（從0開始）
     * @param size 每頁大小
     * @return 商品分頁結果
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根據分類獲取商品", description = "分頁獲取指定分類下的商品列表")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 查找分類
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("找不到分類"));
        
        // 創建分頁參數
        Pageable pageable = PageRequest.of(page, size);
        
        // 查詢商品
        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        
        // 將Product實體轉換為ProductResponse DTO
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productResponses);
    }

    /**
     * 根據關鍵字搜索商品（分頁）
     * @param keyword 搜索關鍵字
     * @param page 頁碼（從0開始）
     * @param size 每頁大小
     * @return 商品分頁結果
     */
    @GetMapping("/search")
    @Operation(summary = "搜索商品", description = "根據關鍵字搜索商品")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // 創建分頁參數
        Pageable pageable = PageRequest.of(page, size);
        
        // 搜索商品
        Page<Product> productPage = productRepository.searchByKeyword(keyword, pageable);
        
        // 將Product實體轉換為ProductResponse DTO
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(productResponses);
    }

    /**
     * 根據ID獲取商品詳情
     * @param id 商品ID
     * @return 商品詳情
     */
    @GetMapping("/{id}")
    @Operation(summary = "獲取商品詳情", description = "根據ID獲取商品的詳細資訊")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        // 查找商品
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        
        // 將Product實體轉換為ProductResponse DTO
        ProductResponse productResponse = convertToProductResponse(product);
        
        return ResponseEntity.ok(productResponse);
    }

    /**
     * 創建新商品
     * @param productRequest 商品資訊
     * @return 創建結果訊息
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "創建商品", description = "創建新的商品（需要管理員權限）")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        // 查找分類
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("找不到分類"));
        
        // 創建新商品
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(category);
        product.setImageUrl(productRequest.getImageUrl());
        
        // 保存商品
        productRepository.save(product);
        
        return ResponseEntity.ok(new MessageResponse("商品創建成功!"));
    }

    /**
     * 更新商品資訊
     * @param id 商品ID
     * @param productRequest 更新的商品資訊
     * @return 更新結果訊息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "更新商品", description = "更新商品的資訊（需要管理員權限）")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        // 查找商品
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        
        // 查找分類
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("找不到分類"));
        
        // 更新商品資訊
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(category);
        product.setImageUrl(productRequest.getImageUrl());
        
        // 保存更新後的商品
        productRepository.save(product);
        
        return ResponseEntity.ok(new MessageResponse("商品更新成功!"));
    }

    /**
     * 刪除商品
     * @param id 商品ID
     * @return 刪除結果訊息
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "刪除商品", description = "刪除商品（需要管理員權限）")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        // 查找商品
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        
        // 刪除商品
        productRepository.delete(product);
        
        return ResponseEntity.ok(new MessageResponse("商品刪除成功!"));
    }

    /**
     * 將Product實體轉換為ProductResponse DTO
     * @param product Product實體
     * @return ProductResponse DTO
     */
    private ProductResponse convertToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setImageUrl(product.getImageUrl());
        return response;
    }
}