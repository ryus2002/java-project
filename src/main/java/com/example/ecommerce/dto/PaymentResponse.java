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
    
    /**
     * 獲取支付ID
     * @return 支付ID
     */
    public Long getId() {
        return id;
}
    
    /**
     * 設置支付ID
     * @param id 支付ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 獲取支付編號
     * @return 支付編號
     */
    public String getPaymentNumber() {
        return paymentNumber;
    }
    
    /**
     * 設置支付編號
     * @param paymentNumber 支付編號
     */
    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
    
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
     * 獲取訂單編號
     * @return 訂單編號
     */
    public String getOrderNumber() {
        return orderNumber;
    }
    
    /**
     * 設置訂單編號
     * @param orderNumber 訂單編號
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    /**
     * 獲取支付金額
     * @return 支付金額
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * 設置支付金額
     * @param amount 支付金額
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    /**
     * 獲取支付方式
     * @return 支付方式
     */
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    /**
     * 設置支付方式
     * @param paymentMethod 支付方式
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    /**
     * 獲取支付狀態
     * @return 支付狀態
     */
    public PaymentStatus getStatus() {
        return status;
    }
    
    /**
     * 設置支付狀態
     * @param status 支付狀態
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    /**
     * 獲取支付時間
     * @return 支付時間
     */
    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }
    
    /**
     * 設置支付時間
     * @param paymentTime 支付時間
     */
    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    /**
     * 獲取第三方交易編號
     * @return 第三方交易編號
     */
    public String getTransactionId() {
        return transactionId;
    }
    
    /**
     * 設置第三方交易編號
     * @param transactionId 第三方交易編號
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}