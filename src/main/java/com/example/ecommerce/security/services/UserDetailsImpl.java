package com.example.ecommerce.security.services;

import com.example.ecommerce.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用戶詳細信息實現類
 * 實現Spring Security的UserDetails接口，用於表示已認證的用戶
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用戶ID
     */
    private Long id;

    /**
     * 用戶名
     */
    private String username;

    /**
     * 電子郵件
     */
    private String email;

    /**
     * 密碼（在JSON序列化時忽略）
     */
    @JsonIgnore
    private String password;

    /**
     * 用戶權限集合
     */
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * 構造函數
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * 從User實體構建UserDetailsImpl對象
     * @param user User實體
     * @return UserDetailsImpl對象
     */
    public static UserDetailsImpl build(User user) {
        // 將用戶角色轉換為GrantedAuthority對象
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // 創建並返回UserDetailsImpl對象
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    /**
     * 獲取用戶權限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 獲取用戶ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 獲取用戶電子郵件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 獲取用戶密碼
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 獲取用戶名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 判斷賬號是否未過期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 判斷賬號是否未鎖定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 判斷憑證是否未過期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判斷賬號是否啟用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 判斷兩個用戶是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}