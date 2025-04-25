package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 購物車回應DTO
 * 用於向客戶端返回購物車資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    /**
     * 購物車ID
     */
    private Long id;
    
    /**
     * 使用者ID
     */
    private Long userId;
    
    /**
     * 購物車中的商品項目列表
     */
    private List<CartItemResponse> items = new ArrayList<>();
    
    /**
     * 購物車總金額
     */
    private BigDecimal totalAmount;
    
    /**
     * 購物車中的商品總數量
     */
    private Integer totalItems;
}