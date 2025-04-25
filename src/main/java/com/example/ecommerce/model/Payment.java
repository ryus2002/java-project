package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付記錄實體類
 * 用於存儲訂單的支付資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    /**
     * 支付記錄ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 支付編號，唯一
     */
    @Column(unique = true)
    private String paymentNumber;

    /**
     * 關聯的訂單，一對一關係
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    /**
     * 支付金額
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    /**
     * 支付狀態
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * 支付時間
     */
    private LocalDateTime paymentTime;

    /**
     * 第三方支付平台的交易編號
     */
    private String transactionId;
}