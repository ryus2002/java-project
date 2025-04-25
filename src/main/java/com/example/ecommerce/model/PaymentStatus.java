package com.example.ecommerce.model;

/**
 * 支付狀態枚舉類型
 * 定義支付的各種狀態
 */
public enum PaymentStatus {
    /**
     * 待支付：尚未完成支付
     */
    PENDING,
    
    /**
     * 已支付：支付已完成
     */
    COMPLETED,
    
    /**
     * 失敗：支付失敗
     */
    FAILED,
    
    /**
     * 已退款：支付已退款
     */
    REFUNDED,
    
    /**
     * 部分退款：支付已部分退款
     */
    PARTIALLY_REFUNDED,
    
    /**
     * 已取消：支付已取消
     */
    CANCELLED
}