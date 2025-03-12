package com.management.kbbs.controller;

import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.CommentRepository;
import com.management.kbbs.repository.LoanRecordRepository;
import com.management.kbbs.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRecordRepository loanRecordRepository;
    private final CommentRepository commentRepository;

    // 回傳書籍、用戶、借閱紀錄和評論的總數
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Long>> getAllCounts() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("bookCount", bookRepository.count());
        stats.put("userCount", userRepository.count());
        stats.put("loanRecordCount", loanRecordRepository.count());
        stats.put("commentCount", commentRepository.count());

        return ResponseEntity.ok(stats);
    }
}