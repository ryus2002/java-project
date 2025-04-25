package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分類實體類
 * 用於對商品進行分類管理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    /**
     * 分類ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分類名稱，不可為空且唯一
     */
    @NotBlank
    @Column(length = 50, unique = true)
    private String name;

    /**
     * 分類描述
     */
    @Column(length = 200)
    private String description;

    /**
     * 該分類下的所有商品，一對多關係
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
}
