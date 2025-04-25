package com.example.ecommerce.controller;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 視圖控制器
 * 用於處理前端頁面的路由和數據
 */
@Controller
public class ViewController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartService cartService;

    /**
     * 首頁路由
     * @param model 模型數據
     * @return 首頁視圖名稱
     */
    @GetMapping("/")
    public String index(Model model) {
        // 獲取所有分類
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        
        // 獲取熱門商品（這裡簡單地取最新的4個商品作為熱門商品）
        Pageable featuredPageable = PageRequest.of(0, 4, Sort.by("id").descending());
        Page<Product> featuredProductsPage = productRepository.findAll(featuredPageable);
        model.addAttribute("featuredProducts", featuredProductsPage.getContent());
        
        // 獲取最新商品
        Pageable newPageable = PageRequest.of(0, 4, Sort.by("id").descending());
        Page<Product> newProductsPage = productRepository.findAll(newPageable);
        model.addAttribute("newProducts", newProductsPage.getContent());
        
        // 獲取購物車商品數量
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            Integer cartItemCount = cartService.getCartItemCount();
            model.addAttribute("cartItemCount", cartItemCount);
        }
        
        return "index";
    }

    /**
     * 登入頁面路由
     * @return 登入頁面視圖名稱
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 註冊頁面路由
     * @return 註冊頁面視圖名稱
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 忘記密碼頁面路由
     * @return 忘記密碼頁面視圖名稱
     */
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    /**
     * 個人資料頁面路由
     * @param model 模型數據
     * @return 個人資料頁面視圖名稱
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        // 獲取當前用戶資料
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            // 這裡可以添加用戶資料到模型中
        }
        
        return "profile";
    }

    /**
     * 會員資料頁面路由
     * @param id 會員ID
     * @param model 模型數據
     * @return 會員資料頁面視圖名稱
     */
    @GetMapping("/user/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        // 這裡可以添加會員公開資料到模型中
        
        return "user-profile";
    }
    
    /**
     * 商品列表頁面路由
     * @param model 模型數據
     * @return 商品列表頁面視圖名稱
     */
    @GetMapping("/products")
    public String productList(Model model) {
        // 獲取所有分類
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        
        // 獲取所有商品（分頁）
        Pageable pageable = PageRequest.of(0, 12, Sort.by("id").descending());
        Page<Product> productsPage = productRepository.findAll(pageable);
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", productsPage.getNumber());
        model.addAttribute("totalPages", productsPage.getTotalPages());
        
        return "product-list";
    }
    
    /**
     * 商品詳情頁面路由
     * @param id 商品ID
     * @param model 模型數據
     * @return 商品詳情頁面視圖名稱
     */
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        // 獲取商品詳情
        productRepository.findById(id).ifPresent(product -> {
            model.addAttribute("product", product);
            
            // 獲取相關商品（同一分類的其他商品）
            if (product.getCategory() != null) {
                Pageable pageable = PageRequest.of(0, 4);
                Page<Product> relatedProductsPage = productRepository.findByCategory(
                    product.getCategory(), pageable);
                model.addAttribute("relatedProducts", relatedProductsPage.getContent());
            }
        });
        
        return "product-detail";
    }
    
    /**
     * 購物車頁面路由
     * @param model 模型數據
     * @return 購物車頁面視圖名稱
     */
    @GetMapping("/cart")
    public String cart(Model model) {
        // 獲取購物車資料
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("cart", cartService.getCart());
        }
        
        return "cart";
    }
    
    /**
     * 訂單列表頁面路由
     * @param model 模型數據
     * @return 訂單列表頁面視圖名稱
     */
    @GetMapping("/orders")
    public String orderList(Model model) {
        // 獲取用戶訂單資料
        
        return "order-list";
    }
    
    /**
     * 訂單詳情頁面路由
     * @param id 訂單ID
     * @param model 模型數據
     * @return 訂單詳情頁面視圖名稱
     */
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        // 獲取訂單詳情
        
        return "order-detail";
    }
    
    /**
     * 結帳頁面路由
     * @param model 模型數據
     * @return 結帳頁面視圖名稱
     */
    @GetMapping("/checkout")
    public String checkout(Model model) {
        // 獲取購物車資料
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("cart", cartService.getCart());
        }
        
        return "checkout";
    }
}