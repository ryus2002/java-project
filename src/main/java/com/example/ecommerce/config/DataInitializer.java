package com.example.ecommerce.config;

import com.example.ecommerce.model.ERole;
import com.example.ecommerce.model.Role;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 數據初始化類
 * 在應用程式啟動時初始化必要的數據
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 在應用程式啟動時執行初始化
     */
    @Override
    public void run(String... args) throws Exception {
        // 初始化角色
        initRoles();
        
        // 初始化管理員帳號
        initAdminUser();
    }

    /**
     * 初始化角色
     */
    private void initRoles() {
        // 檢查角色是否已存在
        if (roleRepository.count() == 0) {
            // 創建用戶角色
            Role userRole = new Role(ERole.ROLE_USER);
            roleRepository.save(userRole);

            // 創建管理員角色
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);

            System.out.println("角色初始化完成");
        }
    }

    /**
     * 初始化管理員帳號
     */
    private void initAdminUser() {
        // 檢查管理員帳號是否已存在
        if (!userRepository.existsByUsername("admin")) {
            // 創建管理員帳號
            User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"));
            admin.setNickname("系統管理員");
            // 設置管理員角色
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("錯誤: 找不到管理員角色"));
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);

            System.out.println("管理員帳號初始化完成");
        }
    }
}