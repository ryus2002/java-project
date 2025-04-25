package com.example.ecommerce.repository;

import com.example.ecommerce.model.ERole;
import com.example.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色儲存庫
 * 提供對角色資料的基本CRUD操作
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * 根據角色名稱查找角色
     * @param name 角色名稱
     * @return 包含角色的Optional物件
     */
    Optional<Role> findByName(ERole name);
}