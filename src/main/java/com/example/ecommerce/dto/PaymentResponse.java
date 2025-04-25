package com.example.ecommerce.dto;

import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付回應DTO
 * 用於向客戶端返回支付資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    /**
     * 支付ID
     */
    private Long id;
    
    /**
     * 支付編號
     */
    private String paymentNumber;
    
    /**
     * 訂單ID
     */
    private Long orderId;
    
    /**
     * 訂單編號
     */
    private String orderNumber;
    
    /**
     * 支付金額
     */
    private BigDecimal amount;
    
    /**
     * 支付方式
     */
    private PaymentMethod paymentMethod;
    
    /**
     * 支付狀態
     */
    private PaymentStatus status;
    
    /**
     * 支付時間
     */
    private LocalDateTime paymentTime;
    
    /**
     * 第三方交易編號
     */
    private String transactionId;
}