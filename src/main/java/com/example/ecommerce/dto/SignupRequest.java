package com.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 會員註冊請求DTO
 * 用於接收會員註冊時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    /**
     * 使用者名稱，不可為空且長度在3到20之間
     */
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    /**
     * 電子郵件，不可為空且必須符合電子郵件格式
     */
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    /**
     * 使用者角色集合，可選
     */
    private Set<String> roles;

    /**
     * 密碼，不可為空且長度在6到40之間
     */
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    /**
     * 暱稱，可選
     */
    @Size(max = 50)
    private String nickname;
    
    /**
     * 星座，可選
     */
    @Size(max = 20)
    private String zodiacSign;
    
    /**
     * 獲取使用者名稱
     * @return 使用者名稱
     */
    public String getUsername() {
        return username;
}
    
    /**
     * 設置使用者名稱
     * @param username 使用者名稱
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 獲取電子郵件
     * @return 電子郵件
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 設置電子郵件
     * @param email 電子郵件
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 獲取使用者角色集合
     * @return 角色集合
     */
    public Set<String> getRoles() {
        return roles;
    }
    
    /**
     * 設置使用者角色集合
     * @param roles 角色集合
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    /**
     * 獲取密碼
     * @return 密碼
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 設置密碼
     * @param password 密碼
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 獲取暱稱
     * @return 暱稱
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * 設置暱稱
     * @param nickname 暱稱
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * 獲取星座
     * @return 星座
     */
    public String getZodiacSign() {
        return zodiacSign;
    }
    
    /**
     * 設置星座
     * @param zodiacSign 星座
     */
    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }
}