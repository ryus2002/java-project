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
}