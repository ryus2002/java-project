package com.example.ecommerce.repository;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品儲存庫
 * 提供對商品資料的基本CRUD操作和進階查詢功能
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 根據分類查找商品
     * @param category 商品分類
     * @return 該分類下的所有商品列表
     */
    List<Product> findByCategory(Category category);
    
    /**
     * 根據分類分頁查找商品
     * @param category 商品分類
     * @param pageable 分頁參數
     * @return 該分類下的商品分頁結果
     */
    Page<Product> findByCategory(Category category, Pageable pageable);
    
    /**
     * 根據商品名稱模糊查詢商品
     * @param name 商品名稱關鍵字
     * @param pageable 分頁參數
     * @return 符合條件的商品分頁結果
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    /**
     * 根據商品名稱或描述模糊查詢商品
     * @param keyword 關鍵字
     * @param pageable 分頁參數
     * @return 符合條件的商品分頁結果
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根據分類和關鍵字查詢商品
     * @param category 商品分類
     * @param keyword 關鍵字
     * @param pageable 分頁參數
     * @return 符合條件的商品分頁結果
     */
    @Query("SELECT p FROM Product p WHERE p.category = :category AND (p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<Product> searchByCategoryAndKeyword(@Param("category") Category category, @Param("keyword") String keyword, Pageable pageable);
}