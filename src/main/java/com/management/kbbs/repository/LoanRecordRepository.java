package com.management.kbbs.repository;

import com.management.kbbs.entity.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {
    // 你可以在這裡添加自定義查詢方法，例如：
    // List<LoanRecord> findByUserId(Long userId);
    // List<LoanRecord> findByBookId(Long bookId);
}

