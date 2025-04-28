package com.example.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品實體類
 * 用於存儲商品的基本資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    /**
     * 商品ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名稱，不可為空
     */
    @NotBlank
    @Column(length = 100)
    private String name;

    /**
     * 商品描述，可以是長文本
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 商品價格，必須為正數
     */
    @NotNull
    @Positive
    private BigDecimal price;

    /**
     * 商品庫存，必須為非負數
     */
    @NotNull
    @PositiveOrZero
    private Integer stock;

    /**
     * 商品分類，多對一關係
     * 使用 @JsonBackReference 避免循環引用
     */
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 商品圖片URL
     */
    private String imageUrl;
}
