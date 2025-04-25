package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 支付請求DTO
 * 用於接收支付訂單時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    /**
     * 訂單ID，不可為空
     */
    @NotNull(message = "訂單ID不能為空")
    private Long orderId;
    
    /**
     * 支付方式，不可為空
     */
    @NotBlank(message = "支付方式不能為空")
    private String paymentMethod;
    
    /**
     * 支付相關的其他資訊，如信用卡號碼等
     */
    private String paymentDetails;
}