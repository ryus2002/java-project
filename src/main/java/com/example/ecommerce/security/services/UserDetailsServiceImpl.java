package com.example.ecommerce.security.services;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用戶詳細信息服務
 * 實現Spring Security的UserDetailsService接口，用於加載用戶信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    /**
     * 根據用戶名加載用戶詳細信息
     * @param username 用戶名
     * @return 用戶詳細信息
     * @throws UsernameNotFoundException 如果用戶不存在則拋出此異常
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 從數據庫中查找用戶
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("找不到用戶名為: " + username + " 的用戶"));

        // 構建並返回UserDetailsImpl對象
        return UserDetailsImpl.build(user);
    }
}