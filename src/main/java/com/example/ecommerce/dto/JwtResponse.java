package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 會員登入回應DTO
 * 用於向客戶端返回登入成功後的資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    /**
     * JWT令牌
     */
    private String token;
    
    /**
     * 令牌類型，通常為"Bearer"
     */
    private String type = "Bearer";
    
    /**
     * 使用者ID
     */
    private Long id;
    
    /**
     * 使用者名稱
     */
    private String username;
    
    /**
     * 使用者電子郵件
     */
    private String email;
    
    /**
     * 使用者角色列表
     */
    private List<String> roles;

    /**
     * 建構子，用於創建JWT回應
     */
    public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}