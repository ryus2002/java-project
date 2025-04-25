package com.example.ecommerce.repository;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 購物車儲存庫
 * 提供對購物車資料的基本CRUD操作
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    /**
     * 根據使用者查找購物車
     * @param user 使用者
     * @return 包含購物車的Optional物件
     */
    Optional<Cart> findByUser(User user);
    
    /**
     * 根據使用者ID查找購物車
     * @param userId 使用者ID
     * @return 包含購物車的Optional物件
     */
    Optional<Cart> findByUserId(Long userId);
}