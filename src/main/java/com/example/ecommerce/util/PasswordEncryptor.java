package com.example.ecommerce.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * 密碼加密工具類
 * 用於加密數據庫密碼和其他敏感信息
 */
public class PasswordEncryptor {
    public static void main(String[] args) {
        // 替換為您的實際資料庫密碼
        String actualDbPassword = "rootpassword";
        
        // 替換為您的實際 JWT 密鑰
        String actualJwtSecret = "c2VjcmV0LWtleS1mb3ItZGV2ZWxvcG1lbnQtZW52aXJvbm1lbnQtc2hvdWxkLWJlLWNoYW5nZWQtaW4tcHJvZHVjdGlvbg==";
        
        // 替換為您的 Jasypt 主密碼
        String jasyptPassword = "your_jasypt_password";
        
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        config.setPassword(jasyptPassword);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        
        encryptor.setConfig(config);
        String encryptedDbPassword = encryptor.encrypt(actualDbPassword);
        String encryptedJwtSecret = encryptor.encrypt(actualJwtSecret);
        System.out.println("加密後的資料庫密碼: " + encryptedDbPassword);
        System.out.println("加密後的JWT密鑰: " + encryptedJwtSecret);
        
        // 資料庫配置
        System.out.println("spring.datasource.password=ENC(" + encryptedDbPassword + ")");
        
        // JWT配置
        System.out.println("jwt.secret=ENC(" + encryptedJwtSecret + ")");
    }
}