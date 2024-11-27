package com.management.kbbs.repository;

import com.management.kbbs.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 根據 email 查詢用戶（已自動提供基本 CRUD 操作）
    Optional<Member> findByEmail(String email);
}
