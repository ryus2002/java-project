package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 會員登入請求DTO
 * 用於接收會員登入時提交的資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    /**
     * 使用者名稱，不可為空
     */
    @NotBlank
    private String username;

    /**
     * 密碼，不可為空
     */
    @NotBlank
    private String password;
    
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
}
