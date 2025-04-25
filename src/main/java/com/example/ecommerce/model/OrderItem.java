package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 訂單項目實體類
 * 用於存儲訂單中的單個商品項目
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    /**
     * 訂單項目ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所屬的訂單，多對一關係
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 商品ID，記錄商品的ID
     */
    private Long productId;

    /**
     * 商品名稱，記錄下單時的商品名稱
     */
    private String productName;

    /**
     * 商品單價，記錄下單時的商品價格
     */
    @NotNull
    private BigDecimal price;

    /**
     * 商品數量，必須為正數
     */
    @NotNull
    @Positive
    private Integer quantity;

    /**
     * 商品圖片URL，記錄下單時的商品圖片
     */
    private String imageUrl;
}
