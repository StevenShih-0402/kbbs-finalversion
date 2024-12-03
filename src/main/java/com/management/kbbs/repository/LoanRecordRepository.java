package com.management.kbbs.repository;

import com.management.kbbs.entity.LoanRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRecordRepository extends JpaRepository<LoanRecord, Long> {

}

