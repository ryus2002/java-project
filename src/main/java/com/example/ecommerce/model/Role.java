package com.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色實體類
 * 用於定義使用者的角色和權限
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    /**
     * 角色ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名稱，使用枚舉類型定義
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
    
    /**
     * 建構子，用於創建新角色
     */
    public Role(ERole name) {
        this.name = name;
    }
}
