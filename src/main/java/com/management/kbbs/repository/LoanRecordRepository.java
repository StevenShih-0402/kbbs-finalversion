package com.management.kbbs.repository;

import com.management.kbbs.dto.BookPopularDTO;
import com.management.kbbs.dto.UserActivityDTO;
import com.management.kbbs.entity.LoanRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {
    // 熱門書籍排行(列出借閱次數最多的書籍)
    @Query("SELECT new com.management.kbbs.dto.BookPopularDTO(b.id, b.title, b.author, COUNT(lr.id) as borrowCount) " +
            "FROM LoanRecord lr " +
            "JOIN lr.book b " +
            "GROUP BY b.id, b.title, b.author " +
            "ORDER BY borrowCount DESC")
    List<BookPopularDTO> findPopularBooks(Pageable pageable);

    // 活躍用戶排行(列出借閱次數最多的用戶)
    @Query("SELECT new com.management.kbbs.dto.UserActivityDTO(u.name, COUNT(lr)) " +
            "FROM LoanRecord lr " +
            "JOIN lr.user u " +
            "GROUP BY u.id, u.name " +
            "ORDER BY COUNT(lr) DESC")
    List<UserActivityDTO> findActiveUsers(Pageable pageable);
}

