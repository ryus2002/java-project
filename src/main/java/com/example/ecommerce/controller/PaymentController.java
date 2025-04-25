package com.example.ecommerce.controller;

import com.example.ecommerce.dto.MessageResponse;
import com.example.ecommerce.dto.PaymentRequest;
import com.example.ecommerce.dto.PaymentResponse;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 支付控制器
 * 處理訂單支付相關的API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payments")
@Tag(name = "支付管理", description = "訂單支付相關的API")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 處理訂單支付
     * @param paymentRequest 支付請求資料
     * @return 支付結果資訊
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Transactional
    @Operation(summary = "處理訂單支付", description = "處理用戶的訂單支付請求")
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找訂單
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("找不到訂單"));
        
        // 檢查權限（只有訂單所屬用戶可以支付）
        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限支付此訂單!"));
        }
        
        // 檢查訂單狀態（只有待付款的訂單可以支付）
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 該訂單狀態不允許支付!"));
        }
        
        // 檢查訂單是否已經支付
        if (order.getIsPaid()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 該訂單已經支付!"));
        }
        
        // 檢查是否已存在支付記錄
        if (paymentRepository.findByOrder(order).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 該訂單已有支付記錄!"));
        }
        
        // 創建支付記錄
        Payment payment = new Payment();
        payment.setPaymentNumber(generatePaymentNumber());
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        
        // 設置支付方式
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(paymentRequest.getPaymentMethod());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無效的支付方式!"));
        }
        payment.setPaymentMethod(paymentMethod);
        
        // 在實際系統中，這裡應該調用第三方支付服務進行實際支付處理
        // 為了演示，我們假設支付成功
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        // 保存支付記錄
        Payment savedPayment = paymentRepository.save(payment);
        
        // 更新訂單狀態
        order.setStatus(OrderStatus.PAID);
        order.setIsPaid(true);
        order.setPaidAt(LocalDateTime.now());
        orderRepository.save(order);
        
        // 轉換為DTO返回
        PaymentResponse paymentResponse = convertToPaymentResponse(savedPayment);
        
        return ResponseEntity.ok(paymentResponse);
    }

    /**
     * 獲取訂單的支付記錄
     * @param orderId 訂單ID
     * @return 支付記錄資訊
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取訂單支付記錄", description = "獲取指定訂單的支付記錄")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Long orderId) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找訂單
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));
        
        // 檢查權限（只有訂單所屬用戶或管理員可以查看）
        if (!order.getUser().getId().equals(user.getId()) && 
                !user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限查看此支付記錄!"));
        }
        
        // 查找支付記錄
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new RuntimeException("找不到支付記錄"));
        
        // 轉換為DTO返回
        PaymentResponse paymentResponse = convertToPaymentResponse(payment);
        
        return ResponseEntity.ok(paymentResponse);
    }

    /**
     * 生成支付編號
     * @return 支付編號
     */
    private String generatePaymentNumber() {
        // 使用UUID生成唯一的支付編號
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 獲取當前登入的用戶
     * @return 用戶實體
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("找不到用戶"));
    }

    /**
     * 將Payment實體轉換為PaymentResponse DTO
     * @param payment Payment實體
     * @return PaymentResponse DTO
     */
    private PaymentResponse convertToPaymentResponse(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setPaymentNumber(payment.getPaymentNumber());
        paymentResponse.setOrderId(payment.getOrder().getId());
        paymentResponse.setOrderNumber(payment.getOrder().getOrderNumber());
        paymentResponse.setAmount(payment.getAmount());
        paymentResponse.setPaymentMethod(payment.getPaymentMethod());
        paymentResponse.setStatus(payment.getStatus());
        paymentResponse.setPaymentTime(payment.getPaymentTime());
        paymentResponse.setTransactionId(payment.getTransactionId());
        return paymentResponse;
    }
}