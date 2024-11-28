package com.management.kbbs.repository;

import com.management.kbbs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根據 email 查詢用戶（已自動提供基本 CRUD 操作）
    Optional<User> findByEmail(String email);
}
