package com.example.ecommerce.controller;

import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理員視圖控制器
 * 用於處理管理後台的頁面路由和數據
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminViewController {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * 管理後台首頁路由
     * @param model 模型數據
     * @return 管理後台首頁視圖名稱
     */
    @GetMapping
    public String adminDashboard(Model model) {
        // 統計數據
        long productCount = productRepository.count();
        long categoryCount = categoryRepository.count();
        long orderCount = orderRepository.count();
        long userCount = userRepository.count();
        
        model.addAttribute("productCount", productCount);
        model.addAttribute("categoryCount", categoryCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("userCount", userCount);
        
        return "admin/dashboard";
    }
    
    /**
     * 商品管理頁面路由
     * @param model 模型數據
     * @return 商品管理頁面視圖名稱
     */
    @GetMapping("/products")
    public String productManagement(Model model) {
        return "admin/product-management";
    }
    
    /**
     * 分類管理頁面路由
     * @param model 模型數據
     * @return 分類管理頁面視圖名稱
     */
    @GetMapping("/categories")
    public String categoryManagement(Model model) {
        return "admin/category-management";
    }
    
    /**
     * 訂單管理頁面路由
     * @param model 模型數據
     * @return 訂單管理頁面視圖名稱
     */
    @GetMapping("/orders")
    public String orderManagement(Model model) {
        return "admin/order-management";
    }
    
    /**
     * 會員管理頁面路由
     * @param model 模型數據
     * @return 會員管理頁面視圖名稱
     */
    @GetMapping("/users")
    public String userManagement(Model model) {
        return "admin/user-management";
    }
    
    /**
     * 系統設置頁面路由
     * @param model 模型數據
     * @return 系統設置頁面視圖名稱
     */
    @GetMapping("/settings")
    public String systemSettings(Model model) {
        return "admin/system-settings";
    }
}