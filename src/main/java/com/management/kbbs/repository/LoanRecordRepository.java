package com.management.kbbs.repository;

import com.management.kbbs.dto.PopularBookDTO;
import com.management.kbbs.entity.LoanRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {
    @Query("SELECT new com.management.kbbs.dto.PopularBookDTO(b.id, b.title, b.author, COUNT(lr.id) as borrowCount) " +
            "FROM LoanRecord lr " +
            "JOIN lr.book b " +
            "GROUP BY b.id, b.title, b.author " +
            "ORDER BY borrowCount DESC")
    List<PopularBookDTO> findPopularBooks(Pageable pageable);
}

