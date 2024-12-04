package com.management.kbbs.controller;

import com.management.kbbs.dto.*;
import com.management.kbbs.service.LoanRecordService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/loanrecords")
@RequiredArgsConstructor
public class LoanRecordController {

    private final LoanRecordService loanRecordService;

    // 借書
    @PostMapping("/borrow")
    public ResponseEntity<LoanRecordDTO> borrowBook(@RequestBody LoanRecordRequestDTO requestDTO) {
        LoanRecordDTO createLoanRecord = loanRecordService.borrowBook(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createLoanRecord);
    }

    // 還書
    @PatchMapping("/return/{loanRecordId}")
    public ResponseEntity<LoanRecordDTO> returnBook(@PathVariable Long loanRecordId) {
        LoanRecordDTO updateLoanRecord = loanRecordService.returnBook(loanRecordId);
        return ResponseEntity.ok(updateLoanRecord);
    }

    // 查詢所有借閱紀錄
    @GetMapping
    public ResponseEntity<List<LoanRecordDTO>> getAllLoanRecords() {
        List<LoanRecordDTO> loanRecords = loanRecordService.getAllLoanRecords();
        return ResponseEntity.ok(loanRecords);
    }

    // 查詢單一借閱紀錄
    @GetMapping("/{id}")
    public ResponseEntity<LoanRecordDTO> getLoanRecordById(@PathVariable Long id) {
        LoanRecordDTO loanRecordDTO = loanRecordService.getLoanRecordById(id);
        return ResponseEntity.ok(loanRecordDTO);
    }

    // 更新借閱紀錄
    @PatchMapping("/{id}")
    public ResponseEntity<LoanRecordDTO> updateLoanRecord(@PathVariable Long id, @RequestBody LoanRecordUpdateDTO updateDTO) {
        LoanRecordDTO updatedLoanRecord = loanRecordService.updateLoanRecord(id, updateDTO);
        return ResponseEntity.ok(updatedLoanRecord);
    }

    // 刪除借閱紀錄
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanRecord(@PathVariable Long id) {
        loanRecordService.deleteLoanRecord(id);
        return ResponseEntity.noContent().build();
    }

    // 熱門書籍排行(列出借閱次數最多的書籍)
    @GetMapping("/popularBooks")
    public ResponseEntity<List<BookPopularDTO>> getPopularBooks(@RequestParam(defaultValue = "10") int topN) {
        List<BookPopularDTO> popularBooks = loanRecordService.getPopularBooks(topN);
        return ResponseEntity.ok(popularBooks);
    }

    // 查詢圖書館活躍用戶排行
    @GetMapping("/activeUsers")
    public ResponseEntity<List<UserActivityDTO>> getActiveUsers(@RequestParam(defaultValue = "10") int topN) {
        List<UserActivityDTO> activeUsers = loanRecordService.getActiveUsers(topN);
        return ResponseEntity.ok(activeUsers);
    }

    // 列出未歸還的書籍清單
    @GetMapping("/unreturnedBooks")
    public ResponseEntity<List<BookUnreturnDTO>> getUnreturnedBooks() {
        List<BookUnreturnDTO> unreturnedBooks = loanRecordService.getUnreturnedBooks();
        return ResponseEntity.ok(unreturnedBooks);
    }
}