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
    
    /**
     * 獲取訂單ID
     * @return 訂單ID
     */
    public Long getOrderId() {
        return orderId;
}
    
    /**
     * 設置訂單ID
     * @param orderId 訂單ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    /**
     * 獲取支付方式
     * @return 支付方式
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * 設置支付方式
     * @param paymentMethod 支付方式
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * 獲取支付相關的其他資訊
     * @return 支付相關的其他資訊
     */
    public String getPaymentDetails() {
        return paymentDetails;
    }
    
    /**
     * 設置支付相關的其他資訊
     * @param paymentDetails 支付相關的其他資訊
     */
    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}