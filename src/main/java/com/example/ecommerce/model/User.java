package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 使用者實體類
 * 用於存儲會員的基本資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class User {
    /**
     * 使用者ID，主鍵
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 使用者名稱，不可為空
     */
    @NotBlank
    @Size(max = 20)
    private String username;

    /**
     * 使用者電子郵件，不可為空且必須符合電子郵件格式
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * 使用者密碼，不可為空且會被加密存儲
     */
    @NotBlank
    @Size(max = 120)
    private String password;
    
    /**
     * 使用者暱稱，可選
     */
    @Size(max = 50)
    private String nickname;
    
    /**
     * 使用者星座，可選
     */
    @Size(max = 20)
    private String zodiacSign;
    
    /**
     * 使用者角色，多對多關係
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
              joinColumns = @JoinColumn(name = "user_id"),
              inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    /**
     * 建構子，用於創建新使用者
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
