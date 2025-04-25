package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 使用者儲存庫
 * 提供對使用者資料的基本CRUD操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根據使用者名稱查找使用者
     * @param username 使用者名稱
     * @return 包含使用者的Optional物件
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根據電子郵件查找使用者
     * @param email 電子郵件
     * @return 包含使用者的Optional物件
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 檢查使用者名稱是否已存在
     * @param username 使用者名稱
     * @return 如果存在返回true，否則返回false
     */
    Boolean existsByUsername(String username);
    
    /**
     * 檢查電子郵件是否已存在
     * @param email 電子郵件
     * @return 如果存在返回true，否則返回false
     */
    Boolean existsByEmail(String email);
}