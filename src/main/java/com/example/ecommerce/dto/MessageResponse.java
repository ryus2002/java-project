package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用回應訊息DTO
 * 用於向客戶端返回操作結果的訊息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    /**
     * 回應訊息內容
     */
    private String message;
}