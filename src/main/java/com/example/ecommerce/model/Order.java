package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 訂單實體類
 * 用於存儲使用者的訂單資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    /**
     * 訂單ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 訂單編號，唯一
     */
    @Column(unique = true)
    private String orderNumber;

    /**
     * 訂單所屬的使用者，多對一關係
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 訂單中的商品項目，一對多關係
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 訂單總金額
     */
    private BigDecimal totalAmount;

    /**
     * 訂單狀態
     */
    @Enumerated(EnumType.STRING)
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
    private Boolean isPaid = false;

    /**
     * 支付時間
     */
    private LocalDateTime paidAt;
    
    /**
     * 計算訂單總金額的方法
     */
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
