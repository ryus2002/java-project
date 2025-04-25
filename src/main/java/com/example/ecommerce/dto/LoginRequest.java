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
}