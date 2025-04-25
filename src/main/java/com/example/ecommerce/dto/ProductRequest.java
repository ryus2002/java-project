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
}