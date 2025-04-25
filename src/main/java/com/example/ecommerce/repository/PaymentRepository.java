package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.model.PaymentMethod;
import com.example.ecommerce.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 支付記錄儲存庫
 * 提供對支付記錄資料的基本CRUD操作和進階查詢功能
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * 根據支付編號查找支付記錄
     * @param paymentNumber 支付編號
     * @return 包含支付記錄的Optional物件
     */
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    /**
     * 根據訂單查找支付記錄
     * @param order 訂單
     * @return 包含支付記錄的Optional物件
     */
    Optional<Payment> findByOrder(Order order);
    
    /**
     * 根據訂單ID查找支付記錄
     * @param orderId 訂單ID
     * @return 包含支付記錄的Optional物件
     */
    Optional<Payment> findByOrderId(Long orderId);
    
    /**
     * 根據支付狀態查找支付記錄
     * @param status 支付狀態
     * @param pageable 分頁參數
     * @return 符合狀態的支付記錄分頁結果
     */
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    /**
     * 根據支付方式查找支付記錄
     * @param paymentMethod 支付方式
     * @param pageable 分頁參數
     * @return 符合支付方式的支付記錄分頁結果
     */
    Page<Payment> findByPaymentMethod(PaymentMethod paymentMethod, Pageable pageable);
    
    /**
     * 根據支付時間範圍查找支付記錄
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param pageable 分頁參數
     * @return 符合時間範圍的支付記錄分頁結果
     */
    Page<Payment> findByPaymentTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    /**
     * 根據第三方交易編號查找支付記錄
     * @param transactionId 第三方交易編號
     * @return 包含支付記錄的Optional物件
     */
    Optional<Payment> findByTransactionId(String transactionId);
}