package com.example.ecommerce.security.jwt;

import com.example.ecommerce.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT工具類
 * 用於生成、解析和驗證JWT令牌
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * JWT密鑰，從配置文件中讀取
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * JWT過期時間（毫秒），從配置文件中讀取
     */
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    /**
     * 根據用戶認證信息生成JWT令牌
     * @param authentication 用戶認證信息
     * @return JWT令牌字符串
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        
        logger.debug("Generating JWT token for user: {}", userPrincipal.getUsername());
        logger.debug("Using JWT secret: {}", jwtSecret);

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 從JWT密鑰字符串生成密鑰對象
     * @return 密鑰對象
     */
    private Key key() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Failed to decode JWT secret: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT secret key", e);
        }
    }

    /**
     * 從JWT令牌中獲取用戶名
     * @param token JWT令牌
     * @return 用戶名
     */
    public String getUserNameFromJwtToken(String token) {
        try {
            String username = Jwts.parserBuilder().setSigningKey(key()).build()
                    .parseClaimsJws(token).getBody().getSubject();
            logger.debug("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 驗證JWT令牌是否有效
     * @param authToken JWT令牌
     * @return 如果有效返回true，否則返回false
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            logger.debug("JWT token validated successfully");
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }

        return false;
    }
}