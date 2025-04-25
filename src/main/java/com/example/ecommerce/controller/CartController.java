package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartItemRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.dto.CartResponse;
import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 購物車控制器
 * 處理購物車的管理API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
@Tag(name = "購物車管理", description = "購物車的管理API")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 獲取當前用戶的購物車
     * @return 購物車資訊
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取購物車", description = "獲取當前用戶的購物車資訊")
    public ResponseEntity<CartResponse> getCart() {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找或創建用戶的購物車
        Cart cart = getOrCreateCart(user);
        
        // 將Cart實體轉換為CartResponse DTO
        CartResponse cartResponse = convertToCartResponse(cart);
        
        return ResponseEntity.ok(cartResponse);
    }

    /**
     * 添加商品到購物車
     * @param cartItemRequest 購物車項目資訊
     * @return 添加結果訊息
     */
    @PostMapping("/items")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "添加商品到購物車", description = "添加商品到當前用戶的購物車")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartItemRequest cartItemRequest) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找或創建用戶的購物車
        Cart cart = getOrCreateCart(user);
        
        // 查找商品
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        
        // 檢查庫存是否足夠
        if (product.getStock() < cartItemRequest.getQuantity()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 商品庫存不足!"));
        }
        
        // 檢查購物車中是否已有該商品
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItem.isPresent()) {
            // 如果購物車中已有該商品，則更新數量
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + cartItemRequest.getQuantity();
            
            // 再次檢查庫存是否足夠
            if (product.getStock() < newQuantity) {
                return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 商品庫存不足!"));
            }
            
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            // 如果購物車中沒有該商品，則創建新的購物車項目
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItemRepository.save(cartItem);
        }
        
        return ResponseEntity.ok(new MessageResponse("商品已添加到購物車!"));
    }

    /**
     * 更新購物車項目數量
     * @param itemId 購物車項目ID
     * @param cartItemRequest 更新的購物車項目資訊
     * @return 更新結果訊息
     */
    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "更新購物車項目", description = "更新購物車中商品的數量")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId, @Valid @RequestBody CartItemRequest cartItemRequest) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找用戶的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));
        
        // 查找購物車項目
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車項目"));
        
        // 驗證該購物車項目是否屬於當前用戶
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限修改此購物車項目!"));
        }
        
        // 查找商品
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("找不到商品"));
        
        // 檢查庫存是否足夠
        if (product.getStock() < cartItemRequest.getQuantity()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 商品庫存不足!"));
        }
        
        // 更新購物車項目
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItemRepository.save(cartItem);
        
        return ResponseEntity.ok(new MessageResponse("購物車項目已更新!"));
    }

    /**
     * 從購物車中刪除項目
     * @param itemId 購物車項目ID
     * @return 刪除結果訊息
     */
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "刪除購物車項目", description = "從購物車中刪除商品")
    public ResponseEntity<?> removeCartItem(@PathVariable Long itemId) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找用戶的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));
        
        // 查找購物車項目
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("找不到購物車項目"));
        
        // 驗證該購物車項目是否屬於當前用戶
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限刪除此購物車項目!"));
        }
        
        // 刪除購物車項目
        cartItemRepository.delete(cartItem);
        
        return ResponseEntity.ok(new MessageResponse("商品已從購物車中刪除!"));
    }

    /**
     * 清空購物車
     * @return 清空結果訊息
     */
    @DeleteMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "清空購物車", description = "清空當前用戶的購物車")
    public ResponseEntity<?> clearCart() {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找用戶的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));
        
        // 刪除購物車中的所有項目
        cartItemRepository.deleteByCart(cart);
        
        return ResponseEntity.ok(new MessageResponse("購物車已清空!"));
    }

    /**
     * 獲取當前登入的用戶
     * @return 用戶實體
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
    }

    /**
     * 查找或創建用戶的購物車
     * @param user 用戶實體
     * @return 購物車實體
     */
    private Cart getOrCreateCart(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
    }

    /**
     * 將Cart實體轉換為CartResponse DTO
     * @param cart Cart實體
     * @return CartResponse DTO
     */
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setUserId(cart.getUser().getId());
        
        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;
        
        for (CartItem item : cart.getCartItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());
            
            // 計算小計金額
            BigDecimal subtotal = item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
            itemResponse.setSubtotal(subtotal);
            
            itemResponse.setImageUrl(item.getProduct().getImageUrl());
            
            itemResponses.add(itemResponse);
            
            // 累加總金額和總數量
            totalAmount = totalAmount.add(subtotal);
            totalItems += item.getQuantity();
        }
        
        cartResponse.setItems(itemResponses);
        cartResponse.setTotalAmount(totalAmount);
        cartResponse.setTotalItems(totalItems);
        
        return cartResponse;
    }
}