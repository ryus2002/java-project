package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 訂單項目回應DTO
 * 用於向客戶端返回訂單項目資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    /**
     * 訂單項目ID
     */
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名稱
     */
    private String productName;
    
    /**
     * 商品單價
     */
    private BigDecimal price;
    
    /**
     * 商品數量
     */
    private Integer quantity;
    
    /**
     * 項目小計金額
     */
    private BigDecimal subtotal;
    
    /**
     * 商品圖片URL
     */
    private String imageUrl;
}