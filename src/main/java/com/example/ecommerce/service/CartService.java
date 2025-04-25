package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 購物車服務
 * 處理購物車相關的業務邏輯
 */
@Service
public class CartService {

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
     * @return 購物車對象
     */
    public Cart getCart() {
        User user = getCurrentUser();
        if (user == null) {
            return null;
        }

        // 查找用戶的購物車，如果不存在則創建一個新的購物車
        Optional<Cart> cartOptional = cartRepository.findByUser(user);
        Cart cart;
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        return cart;
    }

    /**
     * 獲取購物車中的商品數量
     * @return 購物車中的商品數量
     */
    public Integer getCartItemCount() {
        Cart cart = getCart();
        if (cart == null) {
            return 0;
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            return 0;
        }

        return cartItems.size();
    }

    /**
     * 添加商品到購物車
     * @param productId 商品ID
     * @param quantity 數量
     * @return 更新後的購物車
     */
    @Transactional
    public Cart addItemToCart(Long productId, Integer quantity) {
        // 獲取當前用戶的購物車
        Cart cart = getCart();
        if (cart == null) {
            throw new RuntimeException("用戶未登入");
        }

        // 查找商品
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));

        // 檢查商品庫存
        if (product.getStock() < quantity) {
            throw new RuntimeException("商品庫存不足");
        }

        // 查找購物車中是否已存在該商品
        Optional<CartItem> existingItemOptional = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItemOptional.isPresent()) {
            // 如果購物車中已存在該商品，則更新數量
            CartItem existingItem = existingItemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // 如果購物車中不存在該商品，則添加新的購物車項目
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    /**
     * 更新購物車中商品的數量
     * @param itemId 購物車項目ID
     * @param quantity 新數量
     * @return 更新後的購物車
     */
    @Transactional
    public Cart updateCartItemQuantity(Long itemId, Integer quantity) {
        // 獲取當前用戶的購物車
        Cart cart = getCart();
        if (cart == null) {
            throw new RuntimeException("用戶未登入");
        }

        // 查找購物車項目
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("購物車項目不存在"));

        // 檢查該購物車項目是否屬於當前用戶的購物車
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("無權操作此購物車項目");
        }

        // 檢查商品庫存
        if (cartItem.getProduct().getStock() < quantity) {
            throw new RuntimeException("商品庫存不足");
        }

        // 更新數量
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return cart;
    }

    /**
     * 從購物車中移除商品
     * @param itemId 購物車項目ID
     * @return 更新後的購物車
     */
    @Transactional
    public Cart removeCartItem(Long itemId) {
        // 獲取當前用戶的購物車
        Cart cart = getCart();
        if (cart == null) {
            throw new RuntimeException("用戶未登入");
        }

        // 查找購物車項目
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("購物車項目不存在"));

        // 檢查該購物車項目是否屬於當前用戶的購物車
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("無權操作此購物車項目");
        }

        // 刪除購物車項目
        cartItemRepository.delete(cartItem);

        return cart;
    }

    /**
     * 清空購物車
     * @return 清空後的購物車
     */
    @Transactional
    public Cart clearCart() {
        // 獲取當前用戶的購物車
        Cart cart = getCart();
        if (cart == null) {
            throw new RuntimeException("用戶未登入");
        }

        // 刪除購物車中的所有項目
        cartItemRepository.deleteByCart(cart);

        return cart;
    }

    /**
     * 獲取當前登入的用戶
     * @return 當前用戶
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("用戶不存在"));
    }
}