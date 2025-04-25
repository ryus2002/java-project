package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品請求DTO
 * 用於接收創建或更新商品時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    /**
     * 商品名稱，不可為空
     */
    @NotBlank(message = "商品名稱不能為空")
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品價格，必須為正數
     */
    @NotNull(message = "商品價格不能為空")
    @Positive(message = "商品價格必須大於零")
    private BigDecimal price;

    /**
     * 商品庫存，必須為非負數
     */
    @NotNull(message = "商品庫存不能為空")
    @PositiveOrZero(message = "商品庫存不能為負數")
    private Integer stock;

    /**
     * 商品分類ID，不可為空
     */
    @NotNull(message = "商品分類不能為空")
    private Long categoryId;

    /**
     * 商品圖片URL
     */
    private String imageUrl;

    /**
     * 獲取商品名稱
     * @return 商品名稱
     */
    public String getName() {
        return name;
    }

    /**
     * 設置商品名稱
     * @param name 商品名稱
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 獲取商品描述
     * @return 商品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 設置商品描述
     * @param description 商品描述
     */
    public void setDescription(String description) {
        this.description = description;
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
     * 獲取商品庫存
     * @return 商品庫存
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * 設置商品庫存
     * @param stock 商品庫存
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 獲取商品分類ID
     * @return 商品分類ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 設置商品分類ID
     * @param categoryId 商品分類ID
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
