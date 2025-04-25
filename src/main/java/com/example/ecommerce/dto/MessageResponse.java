package com.example.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用回應訊息DTO
 * 用於向客戶端返回操作結果的訊息
 */
@Data
@NoArgsConstructor
public class MessageResponse {
    /**
     * 回應訊息內容
     */
    private String message;
    
    /**
     * 帶參數的構造函數
     * @param message 回應訊息內容
     */
    public MessageResponse(String message) {
        this.message = message;
    }
    
    /**
     * 獲取回應訊息內容
     * @return 回應訊息內容
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 設置回應訊息內容
     * @param message 回應訊息內容
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
