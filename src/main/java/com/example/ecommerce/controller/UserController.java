package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用戶控制器
 * 處理會員資料的查詢和修改的API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Tag(name = "用戶管理", description = "會員資料的查詢和修改的API")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * 獲取當前登入會員的資料
     * @return 會員資料
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取當前會員資料", description = "獲取當前登入會員的詳細資料")
    public ResponseEntity<?> getCurrentUser() {
        // 獲取當前登入的用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // 從數據庫中查詢完整的用戶資料
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
        
        // 創建返回的資料映射，排除敏感資訊
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("nickname", user.getNickname());
        response.put("zodiacSign", user.getZodiacSign());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根據ID獲取會員的公開資料
     * @param id 會員ID
     * @return 會員公開資料
     */
    @GetMapping("/{id}")
    @Operation(summary = "獲取會員公開資料", description = "根據ID獲取會員的公開資料")
    public ResponseEntity<?> getUserPublicProfile(@PathVariable Long id) {
        // 從數據庫中查詢用戶資料
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
        
        // 創建返回的資料映射，只包含公開資訊
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("nickname", user.getNickname());
        response.put("zodiacSign", user.getZodiacSign());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 更新當前會員的資料
     * @param updates 要更新的資料
     * @return 更新結果訊息
     */
    @PutMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "更新會員資料", description = "更新當前登入會員的資料")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody Map<String, String> updates) {
        // 獲取當前登入的用戶
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // 從數據庫中查詢完整的用戶資料
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
        
        // 更新用戶資料
        if (updates.containsKey("email")) {
            // 檢查電子郵件是否已被其他用戶使用
            String newEmail = updates.get("email");
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 電子郵件已被使用!"));
            }
            user.setEmail(newEmail);
        }
        
        if (updates.containsKey("password")) {
            // 更新密碼（加密後存儲）
            user.setPassword(encoder.encode(updates.get("password")));
        }
        
        if (updates.containsKey("nickname")) {
            user.setNickname(updates.get("nickname"));
        }
        
        if (updates.containsKey("zodiacSign")) {
            user.setZodiacSign(updates.get("zodiacSign"));
        }
        
        // 保存更新後的用戶資料
        userRepository.save(user);
        
        return ResponseEntity.ok(new MessageResponse("用戶資料更新成功!"));
    }
}