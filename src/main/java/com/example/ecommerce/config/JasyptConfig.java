package com.example.ecommerce.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jasypt 加密配置類
 * 用於配置數據庫密碼等敏感資訊的加密
 */
@Configuration
public class JasyptConfig {

    /**
     * 從環境變數或配置文件中獲取加密密鑰
     */
    @Value("${jasypt.encryptor.password:your_jasypt_password}")
    private String password;

    /**
     * 配置 Jasypt 加密器
     * @return StringEncryptor 加密器
     */
    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        // 設置加密密鑰
        config.setPassword(password);
        
        // 設置加密算法
        config.setAlgorithm("PBEWithMD5AndDES");
        
        // 設置密鑰獲取迭代次數
        config.setKeyObtentionIterations("1000");
        
        // 設置加密操作的池大小
        config.setPoolSize("1");
        
        // 設置提供者名稱
        config.setProviderName("SunJCE");
        
        // 設置隨機鹽生成器
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        
        // 設置IV生成器
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        
        // 設置字符串輸出編碼格式
        config.setStringOutputType("base64");
        
        encryptor.setConfig(config);
        
        return encryptor;
    }
}