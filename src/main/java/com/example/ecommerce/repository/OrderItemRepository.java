package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 訂單項目儲存庫
 * 提供對訂單項目資料的基本CRUD操作
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * 根據訂單查找所有訂單項目
     * @param order 訂單
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> findByOrder(Order order);
    
    /**
     * 根據訂單ID查找所有訂單項目
     * @param orderId 訂單ID
     * @return 該訂單的所有項目列表
     */
    List<OrderItem> findByOrderId(Long orderId);
    
    /**
     * 根據商品ID查找所有包含該商品的訂單項目
     * @param productId 商品ID
     * @return 包含該商品的所有訂單項目列表
     */
    List<OrderItem> findByProductId(Long productId);
}