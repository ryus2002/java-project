package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品回應DTO
 * 用於向客戶端返回商品資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    /**
     * 商品ID
     */
    private Long id;
    
    /**
     * 商品名稱
     */
    private String name;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品價格
     */
    private BigDecimal price;
    
    /**
     * 商品庫存
     */
    private Integer stock;
    
    /**
     * 商品分類ID
     */
    private Long categoryId;
    
    /**
     * 商品分類名稱
     */
    private String categoryName;
    
    /**
     * 商品圖片URL
     */
    private String imageUrl;
}