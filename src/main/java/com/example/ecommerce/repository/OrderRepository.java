package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 訂單儲存庫
 * 提供對訂單資料的基本CRUD操作和進階查詢功能
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 根據訂單編號查找訂單
     * @param orderNumber 訂單編號
     * @return 包含訂單的Optional物件
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    
    /**
     * 根據使用者查找所有訂單
     * @param user 使用者
     * @return 該使用者的所有訂單列表
     */
    List<Order> findByUser(User user);
    
    /**
     * 根據使用者分頁查找訂單
     * @param user 使用者
     * @param pageable 分頁參數
     * @return 該使用者的訂單分頁結果
     */
    Page<Order> findByUser(User user, Pageable pageable);
    
    /**
     * 根據使用者ID查找所有訂單
     * @param userId 使用者ID
     * @return 該使用者的所有訂單列表
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * 根據使用者ID分頁查找訂單
     * @param userId 使用者ID
     * @param pageable 分頁參數
     * @return 該使用者的訂單分頁結果
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根據訂單狀態查找訂單
     * @param status 訂單狀態
     * @param pageable 分頁參數
     * @return 符合狀態的訂單分頁結果
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    /**
     * 根據使用者和訂單狀態查找訂單
     * @param user 使用者
     * @param status 訂單狀態
     * @return 該使用者符合狀態的訂單列表
     */
    List<Order> findByUserAndStatus(User user, OrderStatus status);
    
    /**
     * 根據訂單創建時間範圍查找訂單
     * @param startDate 開始時間
     * @param endDate 結束時間
     * @param pageable 分頁參數
     * @return 符合時間範圍的訂單分頁結果
     */
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}