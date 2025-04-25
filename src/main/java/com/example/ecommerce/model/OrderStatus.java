package com.example.ecommerce.model;

/**
 * 訂單狀態枚舉類型
 * 定義訂單的各種狀態
 */
public enum OrderStatus {
    /**
     * 待付款：訂單已創建但尚未付款
     */
    PENDING_PAYMENT,
    
    /**
     * 已付款：訂單已付款但尚未發貨
     */
    PAID,
    
    /**
     * 已發貨：訂單已發貨但尚未送達
     */
    SHIPPED,
    
    /**
     * 已完成：訂單已送達並完成
     */
    COMPLETED,
    
    /**
     * 已取消：訂單被取消
     */
    CANCELLED,
    
    /**
     * 退款中：訂單正在處理退款
     */
    REFUNDING,
    
    /**
     * 已退款：訂單已完成退款
     */
    REFUNDED
}