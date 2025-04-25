package com.example.ecommerce.security;

import com.example.ecommerce.security.jwt.AuthEntryPointJwt;
import com.example.ecommerce.security.jwt.AuthTokenFilter;
import com.example.ecommerce.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置類
 * 用於配置安全策略、身份驗證和授權機制
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * 創建JWT身份驗證過濾器
     * @return JWT身份驗證過濾器
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * 創建DAO身份驗證提供者
     * @return DAO身份驗證提供者
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // 設置用戶詳細信息服務
        authProvider.setUserDetailsService(userDetailsService);
        // 設置密碼編碼器
        authProvider.setPasswordEncoder(passwordEncoder());
        
        return authProvider;
    }

    /**
     * 創建身份驗證管理器
     * @param authConfig 身份驗證配置
     * @return 身份驗證管理器
     * @throws Exception 如果創建身份驗證管理器時發生錯誤
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 創建密碼編碼器
     * @return 密碼編碼器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置CORS
     * @return CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 配置安全過濾鏈
     * @param http HTTP安全配置
     * @return 安全過濾鏈
     * @throws Exception 如果配置安全過濾鏈時發生錯誤
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 啟用CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 禁用CSRF保護
            .csrf(AbstractHttpConfigurer::disable)
            // 配置異常處理
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            // 配置會話管理
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置請求授權
            .authorizeHttpRequests(auth -> 
                auth
                    // 允許所有人訪問身份驗證相關的API
                    .requestMatchers("/api/auth/**").permitAll()
                    // 允許所有人訪問公共API
                    .requestMatchers("/api/public/**").permitAll()
                    // 允許所有人訪問API文檔
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/api-docs/**").permitAll()
                    // 允許所有人訪問靜態資源
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                    // 允許所有人訪問公共頁面
                    .requestMatchers("/", "/login", "/register", "/forgot-password").permitAll()
                    .requestMatchers("/products", "/products/**").permitAll()
                    // 其他所有請求需要身份驗證
                    .anyRequest().authenticated()
            );
        
        // 設置身份驗證提供者
        http.authenticationProvider(authenticationProvider());

        // 添加JWT身份驗證過濾器
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}