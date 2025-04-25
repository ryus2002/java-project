package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份驗證檢查控制器
 * 提供檢查用戶身份驗證狀態的API
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "身份驗證檢查", description = "檢查用戶身份驗證狀態的API")
public class AuthCheckController {

    /**
     * 檢查用戶是否已登入
     * @return 用戶身份驗證狀態
     */
    @GetMapping("/check-auth")
    @Operation(summary = "檢查身份驗證狀態", description = "檢查當前用戶是否已登入")
    public ResponseEntity<?> checkAuth() {
        // 獲取當前身份驗證信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 檢查用戶是否已登入
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            
            // 獲取用戶詳細信息
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            // 構建響應數據
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("userId", userDetails.getId());
            response.put("username", userDetails.getUsername());
            response.put("email", userDetails.getEmail());
            response.put("roles", userDetails.getAuthorities());
            
            return ResponseEntity.ok(response);
        } else {
            // 用戶未登入
            return ResponseEntity.status(401).body(new MessageResponse("用戶未登入"));
        }
    }
}