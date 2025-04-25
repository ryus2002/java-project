package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂單回應DTO
 * 用於向客戶端返回訂單資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    /**
     * 訂單ID
     */
    private Long id;
    
    /**
     * 訂單編號
     */
    private String orderNumber;
    
    /**
     * 使用者ID
     */
    private Long userId;
    
    /**
     * 使用者名稱
     */
    private String username;
    
    /**
     * 訂單中的商品項目列表
     */
    private List<OrderItemResponse> items = new ArrayList<>();
    
    /**
     * 訂單總金額
     */
    private BigDecimal totalAmount;
    
    /**
     * 訂單狀態
     */
    private OrderStatus status;
    
    /**
     * 訂單創建時間
     */
    private LocalDateTime orderDate;
    
    /**
     * 收貨地址
     */
    private String shippingAddress;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 是否已支付
     */
    private Boolean isPaid;
    
    /**
     * 支付時間
     */
    private LocalDateTime paidAt;
}