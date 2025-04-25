package com.example.ecommerce.model;

/**
 * 支付方式枚舉類型
 * 定義系統支持的支付方式
 */
public enum PaymentMethod {
    /**
     * 信用卡支付
     */
    CREDIT_CARD,
    
    /**
     * 支付寶支付
     */
    ALIPAY,
    
    /**
     * 微信支付
     */
    WECHAT_PAY,
    
    /**
     * 銀行轉帳
     */
    BANK_TRANSFER,
    
    /**
     * 貨到付款
     */
    CASH_ON_DELIVERY
}