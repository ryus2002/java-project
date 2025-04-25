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
    
    /**
     * 獲取購物車ID
     * @return 購物車ID
     */
    public Long getId() {
        return id;
}
    
    /**
     * 設置購物車ID
     * @param id 購物車ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 獲取使用者ID
     * @return 使用者ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 設置使用者ID
     * @param userId 使用者ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 獲取購物車中的商品項目列表
     * @return 商品項目列表
     */
    public List<CartItemResponse> getItems() {
        return items;
    }
    
    /**
     * 設置購物車中的商品項目列表
     * @param items 商品項目列表
     */
    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
    
    /**
     * 獲取購物車總金額
     * @return 購物車總金額
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * 設置購物車總金額
     * @param totalAmount 購物車總金額
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    /**
     * 獲取購物車中的商品總數量
     * @return 商品總數量
     */
    public Integer getTotalItems() {
        return totalItems;
    }
    
    /**
     * 設置購物車中的商品總數量
     * @param totalItems 商品總數量
     */
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}