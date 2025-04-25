package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 訂單請求DTO
 * 用於接收創建訂單時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    /**
     * 收貨地址，不可為空
     */
    @NotBlank(message = "收貨地址不能為空")
    private String shippingAddress;
    
    /**
     * 支付方式，不可為空
     */
    @NotNull(message = "支付方式不能為空")
    private String paymentMethod;
    
    /**
     * 訂單備註
     */
    private String note;
}