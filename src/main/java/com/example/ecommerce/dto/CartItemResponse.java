package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 購物車項目回應DTO
 * 用於向客戶端返回購物車項目資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    /**
     * 購物車項目ID
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
     * 商品價格
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
    
    /**
     * 獲取購物車項目ID
     * @return 購物車項目ID
     */
    public Long getId() {
        return id;
}
    
    /**
     * 設置購物車項目ID
     * @param id 購物車項目ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 獲取商品ID
     * @return 商品ID
     */
    public Long getProductId() {
        return productId;
    }
    
    /**
     * 設置商品ID
     * @param productId 商品ID
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    /**
     * 獲取商品名稱
     * @return 商品名稱
     */
    public String getProductName() {
        return productName;
    }
    
    /**
     * 設置商品名稱
     * @param productName 商品名稱
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    /**
     * 獲取商品價格
     * @return 商品價格
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * 設置商品價格
     * @param price 商品價格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     * 獲取商品數量
     * @return 商品數量
     */
    public Integer getQuantity() {
        return quantity;
    }
    
    /**
     * 設置商品數量
     * @param quantity 商品數量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    /**
     * 獲取項目小計金額
     * @return 項目小計金額
     */
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    /**
     * 設置項目小計金額
     * @param subtotal 項目小計金額
     */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    /**
     * 獲取商品圖片URL
     * @return 商品圖片URL
     */
    public String getImageUrl() {
        return imageUrl;
    }
    
    /**
     * 設置商品圖片URL
     * @param imageUrl 商品圖片URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}