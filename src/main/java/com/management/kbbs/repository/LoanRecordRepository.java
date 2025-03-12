package com.management.kbbs.repository;

import com.management.kbbs.dto.BookPopularDTO;
import com.management.kbbs.dto.BookUnreturnDTO;
import com.management.kbbs.dto.UserActivityDTO;
import com.management.kbbs.dto.UserSimpleLoanDTO;
import com.management.kbbs.entity.LoanRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {
    // 查詢使用者目前的借閱數量
    @Query("SELECT COUNT(lr) " +
            "FROM LoanRecord lr " +
            "WHERE lr.user.id = :userId AND lr.status = '借閱中'")
    int countActiveLoansByUserId(Long userId);

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

    // 列出未歸還的書籍清單
    @Query("SELECT new com.management.kbbs.dto.BookUnreturnDTO(" +
            "lr.id, lr.book.id, lr.book.title, lr.book.author, lr.user.name, lr.loanDate, lr.dueDate) " +
            "FROM LoanRecord lr " +
            "WHERE lr.status = '借閱中' " +
            "ORDER BY lr.id DESC ")
    List<BookUnreturnDTO> findUnreturnedBooks();

    // 讓使用者查詢簡易的借閱紀錄
    @Query("SELECT new com.management.kbbs.dto.UserSimpleLoanDTO(" +
            "lr.id, lr.book.id, lr.book.title, " +
            "CASE " +
            "   WHEN lr.status = '已歸還' THEN " +
            "       CONCAT('已於 ', lr.returnDate, ' 歸還') " +
            "   WHEN lr.dueDate >= CURRENT_DATE THEN " +
            "       CONCAT('還剩 ', (lr.dueDate - lr.loanDate), ' 天到期') " +
            "   ELSE " +
            "       CONCAT('已到期 ', (CURRENT_DATE - lr.dueDate), ' 天') " +
            "END) " +
            "FROM LoanRecord lr " +
            "JOIN lr.book b " +
            "JOIN lr.user u " +  // 確保正確使用 'user' 欄位
            "WHERE u.name = :username " +
            "ORDER BY lr.id DESC ")
    List<UserSimpleLoanDTO> findSimplifiedLoanRecordsByUserName(@Param("username") String username);
}

