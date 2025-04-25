package com.example.ecommerce.repository;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 購物車項目儲存庫
 * 提供對購物車項目資料的基本CRUD操作
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    /**
     * 根據購物車查找所有購物車項目
     * @param cart 購物車
     * @return 該購物車中的所有項目列表
     */
    List<CartItem> findByCart(Cart cart);
    
    /**
     * 根據購物車ID查找所有購物車項目
     * @param cartId 購物車ID
     * @return 該購物車中的所有項目列表
     */
    List<CartItem> findByCartId(Long cartId);
    
    /**
     * 根據購物車和商品查找購物車項目
     * @param cart 購物車
     * @param product 商品
     * @return 包含購物車項目的Optional物件
     */
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    
    /**
     * 根據購物車ID和商品ID查找購物車項目
     * @param cartId 購物車ID
     * @param productId 商品ID
     * @return 包含購物車項目的Optional物件
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
    /**
     * 刪除購物車中的所有項目
     * @param cart 購物車
     */
    void deleteByCart(Cart cart);
}