package com.example.ecommerce.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 錯誤處理控制器
 * 用於處理系統錯誤並顯示自定義錯誤頁面
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * 處理所有錯誤請求
     * @param request HTTP請求
     * @param model 視圖模型
     * @return 錯誤頁面視圖名稱
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // 獲取錯誤狀態碼
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        
        // 獲取錯誤信息
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "發生未知錯誤";
        }
        
        // 將錯誤信息添加到模型中
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        // 根據錯誤狀態碼返回不同的錯誤頁面
        if (statusCode != null) {
            if (statusCode == 404) {
                return "error/404";
            } else if (statusCode == 403) {
                return "error/403";
            } else if (statusCode == 500) {
                return "error/500";
            }
        }
        
        // 默認錯誤頁面
        return "error/error";
    }
}