package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 商品分類儲存庫
 * 提供對商品分類資料的基本CRUD操作
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * 根據分類名稱查找分類
     * @param name 分類名稱
     * @return 包含分類的Optional物件
     */
    Optional<Category> findByName(String name);
    
    /**
     * 檢查分類名稱是否已存在
     * @param name 分類名稱
     * @return 如果存在返回true，否則返回false
     */
    Boolean existsByName(String name);
}