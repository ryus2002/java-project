package com.example.ecommerce.security.jwt;

import com.example.ecommerce.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT身份驗證過濾器
 * 用於攔截HTTP請求，提取並驗證JWT令牌，並設置Spring Security的身份驗證上下文
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * 過濾HTTP請求，提取並驗證JWT令牌
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 從請求中提取JWT令牌
            String jwt = parseJwt(request);
            
            // 輸出調試信息
            logger.debug("JWT Token: {}", jwt);
            
            // 如果令牌存在且有效，則設置身份驗證上下文
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 從JWT令牌中獲取用戶名
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.debug("Username from token: {}", username);

                // 根據用戶名加載用戶詳細信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 創建身份驗證令牌
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                
                // 設置身份驗證詳細信息
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 設置Spring Security的身份驗證上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set for user: {}", username);
            } else if (jwt != null) {
                logger.warn("Invalid JWT token: {}", jwt);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // 繼續過濾鏈
        filterChain.doFilter(request, response);
    }

    /**
     * 從HTTP請求的Authorization頭部提取JWT令牌
     * @param request HTTP請求
     * @return JWT令牌字符串，如果不存在則返回null
     */
    private String parseJwt(HttpServletRequest request) {
        // 從Authorization頭部獲取JWT令牌
        String headerAuth = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", headerAuth);

        // 如果頭部存在且以"Bearer "開頭，則提取實際的令牌部分
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}