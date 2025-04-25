package com.example.ecommerce.controller;

import com.example.ecommerce.dto.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 訂單控制器
 * 處理訂單的創建、查詢和管理API
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
@Tag(name = "訂單管理", description = "訂單的創建、查詢和管理API")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 創建訂單
     * @param orderRequest 訂單請求資料
     * @return 創建的訂單資訊
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Transactional
    @Operation(summary = "創建訂單", description = "根據購物車內容創建新訂單")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 查找用戶的購物車
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("找不到購物車"));
        
        // 檢查購物車是否為空
        if (cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 購物車為空，無法創建訂單!"));
        }
        
        // 檢查商品庫存是否足夠
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                return ResponseEntity.badRequest().body(
                        new MessageResponse("錯誤: 商品 '" + product.getName() + "' 庫存不足!"));
            }
        }
        
        // 創建訂單
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setIsPaid(false);
        
        // 保存訂單
        Order savedOrder = orderRepository.save(order);
        
        // 創建訂單項目
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setImageUrl(product.getImageUrl());
            
            orderItems.add(orderItem);
            
            // 減少商品庫存
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // 保存訂單項目
        orderItemRepository.saveAll(orderItems);
        
        // 計算訂單總金額
        savedOrder.setOrderItems(orderItems);
        savedOrder.calculateTotalAmount();
        orderRepository.save(savedOrder);
        
        // 清空購物車
        cartItemRepository.deleteByCart(cart);
        
        // 轉換為DTO返回
        OrderResponse orderResponse = convertToOrderResponse(savedOrder);
        
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * 獲取當前用戶的所有訂單
     * @param page 頁碼（從0開始）
     * @param size 每頁大小
     * @param sortBy 排序欄位
     * @param direction 排序方向（asc或desc）
     * @return 訂單列表
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取用戶訂單", description = "獲取當前用戶的所有訂單")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 創建分頁和排序參數
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        // 查詢訂單
        Page<Order> orderPage = orderRepository.findByUser(user, pageable);
        
        // 將Order實體轉換為OrderResponse DTO
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(orderResponses);
    }

    /**
     * 獲取所有訂單（管理員專用）
     * @param page 頁碼（從0開始）
     * @param size 每頁大小
     * @param sortBy 排序欄位
     * @param direction 排序方向（asc或desc）
     * @return 訂單列表
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "獲取所有訂單", description = "獲取系統中的所有訂單（管理員專用）")
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        
        // 創建分頁和排序參數
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        // 查詢訂單
        Page<Order> orderPage = orderRepository.findAll(pageable);
        
        // 將Order實體轉換為OrderResponse DTO
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(orderResponses);
    }

    /**
     * 根據訂單ID獲取訂單詳情
     * @param id 訂單ID
     * @return 訂單詳情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "獲取訂單詳情", description = "根據ID獲取訂單的詳細資訊")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        // 查找訂單
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));
        
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 檢查權限（只有訂單所屬用戶或管理員可以查看）
        if (!order.getUser().getId().equals(user.getId()) && 
                !user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限查看此訂單!"));
        }
        
        // 轉換為DTO返回
        OrderResponse orderResponse = convertToOrderResponse(order);
        
        return ResponseEntity.ok(orderResponse);
    }

    /**
     * 更新訂單狀態（管理員專用）
     * @param id 訂單ID
     * @param status 新的訂單狀態
     * @return 更新結果訊息
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新訂單狀態", description = "更新訂單的狀態（管理員專用）")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        
        // 查找訂單
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));
        
        // 更新訂單狀態
        order.setStatus(status);
        orderRepository.save(order);
        
        return ResponseEntity.ok(new MessageResponse("訂單狀態已更新!"));
    }

    /**
     * 取消訂單
     * @param id 訂單ID
     * @return 取消結果訊息
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Transactional
    @Operation(summary = "取消訂單", description = "取消未支付的訂單")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        // 查找訂單
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));
        
        // 獲取當前登入的用戶
        User user = getCurrentUser();
        
        // 檢查權限（只有訂單所屬用戶或管理員可以取消）
        if (!order.getUser().getId().equals(user.getId()) && 
                !user.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN)) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 無權限取消此訂單!"));
        }
        
        // 檢查訂單狀態（只有待付款的訂單可以取消）
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            return ResponseEntity.badRequest().body(new MessageResponse("錯誤: 只有待付款的訂單可以取消!"));
        }
        
        // 恢復商品庫存
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("找不到商品"));
            
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }
        
        // 更新訂單狀態為已取消
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        
        return ResponseEntity.ok(new MessageResponse("訂單已取消!"));
    }

    /**
     * 生成訂單編號
     * @return 訂單編號
     */
    private String generateOrderNumber() {
        // 使用UUID生成唯一的訂單編號
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
     * 將Order實體轉換為OrderResponse DTO
     * @param order Order實體
     * @return OrderResponse DTO
     */
    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setUsername(order.getUser().getUsername());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setShippingAddress(order.getShippingAddress());
        orderResponse.setPaymentMethod(order.getPaymentMethod());
        orderResponse.setIsPaid(order.getIsPaid());
        orderResponse.setPaidAt(order.getPaidAt());
        
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setPrice(item.getPrice());
            itemResponse.setQuantity(item.getQuantity());
            
            // 計算小計金額
            BigDecimal subtotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            itemResponse.setSubtotal(subtotal);
            
            itemResponse.setImageUrl(item.getImageUrl());
            
            itemResponses.add(itemResponse);
        }
        
        orderResponse.setItems(itemResponses);
        
        return orderResponse;
    }
}