package com.management.kbbs.controller;

import com.management.kbbs.dto.LoanRecordDTO;
import com.management.kbbs.service.LoanRecordService;

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
    public ResponseEntity<LoanRecordDTO> borrowBook(@RequestBody LoanRecordDTO loanRecordDTO) {
        LoanRecordDTO createLoanRecord = loanRecordService.borrowBook(loanRecordDTO);
        return ResponseEntity.ok(createLoanRecord);
    }

    // 還書
    @PatchMapping("/return/{loanRecordId}")
    public ResponseEntity<LoanRecordDTO> returnBook(@PathVariable LoanRecordDTO loanRecordDTO) {
        LoanRecordDTO updateLoanRecord = loanRecordService.returnBook(loanRecordDTO);
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
    @PutMapping("/{id}")
    public ResponseEntity<LoanRecordDTO> updateLoanRecord(@PathVariable Long id, @RequestBody LoanRecordDTO loanRecordDTO) {
        LoanRecordDTO updatedLoanRecord = loanRecordService.updateLoanRecord(id, loanRecordDTO);
        return ResponseEntity.ok(updatedLoanRecord);
    }

    // 刪除借閱紀錄
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanRecord(@PathVariable Long id) {
        loanRecordService.deleteLoanRecord(id);
        return ResponseEntity.noContent().build();
    }
}
