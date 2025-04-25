package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購物車項目請求DTO
 * 用於接收添加商品到購物車或更新購物車項目時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    /**
     * 商品ID，不可為空
     */
    @NotNull(message = "商品ID不能為空")
    private Long productId;
    
    /**
     * 商品數量，必須為正數
     */
    @NotNull(message = "商品數量不能為空")
    @Positive(message = "商品數量必須大於零")
    private Integer quantity;
}