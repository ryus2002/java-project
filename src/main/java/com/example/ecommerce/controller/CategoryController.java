package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品分類控制器
 * 處理商品分類的管理API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
@Tag(name = "商品分類管理", description = "商品分類的增刪改查API")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 獲取所有商品分類
     * @return 商品分類列表
     */
    @GetMapping
    @Operation(summary = "獲取所有分類", description = "獲取所有商品分類的列表")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * 根據ID獲取商品分類
     * @param id 分類ID
     * @return 商品分類資訊
     */
    @GetMapping("/{id}")
    @Operation(summary = "獲取分類詳情", description = "根據ID獲取商品分類的詳細資訊")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到分類"));
        return ResponseEntity.ok(category);
    }

    /**
     * 創建新的商品分類
     * @param category 商品分類資訊
     * @return 創建結果訊息
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "創建分類", description = "創建新的商品分類（需要管理員權限）")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        // 檢查分類名稱是否已存在
        if (categoryRepository.existsByName(category.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("錯誤: 分類名稱已存在!"));
        }

        // 創建新分類
        categoryRepository.save(category);
        return ResponseEntity.ok(new MessageResponse("分類創建成功!"));
    }

    /**
     * 更新商品分類
     * @param id 分類ID
     * @param updates 要更新的資料
     * @return 更新結果訊息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "更新分類", description = "更新商品分類的資訊（需要管理員權限）")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Map<String, String> updates) {
        // 查找要更新的分類
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到分類"));

        // 更新分類資料
        if (updates.containsKey("name")) {
            String newName = updates.get("name");
            // 檢查新名稱是否已被其他分類使用
            if (!newName.equals(category.getName()) && categoryRepository.existsByName(newName)) {
                return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 分類名稱已存在!"));
            }
            category.setName(newName);
        }

        if (updates.containsKey("description")) {
            category.setDescription(updates.get("description"));
        }

        // 保存更新後的分類
        categoryRepository.save(category);
        return ResponseEntity.ok(new MessageResponse("分類更新成功!"));
    }

    /**
     * 刪除商品分類
     * @param id 分類ID
     * @return 刪除結果訊息
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "刪除分類", description = "刪除商品分類（需要管理員權限）")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        // 查找要刪除的分類
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到分類"));

        // 檢查該分類是否有關聯的商品
        if (!category.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 該分類下還有商品，無法刪除!"));
        }

        // 刪除分類
        categoryRepository.delete(category);
        return ResponseEntity.ok(new MessageResponse("分類刪除成功!"));
    }
}