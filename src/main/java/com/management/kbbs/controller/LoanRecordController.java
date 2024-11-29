package com.management.kbbs.controller;

import com.management.kbbs.dto.LoanRecordDTO;
import com.management.kbbs.dto.LoanRecordRequestDTO;
import com.management.kbbs.service.LoanRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loanrecords")
@RequiredArgsConstructor
public class LoanRecordController {

    private final LoanRecordService loanRecordService;

    @PostMapping("/borrow")
    public ResponseEntity<LoanRecordDTO> borrowBook(@RequestBody LoanRecordRequestDTO request) {
        LoanRecordDTO loanRecordDTO = loanRecordService.borrowBook(request.getUserId(), request.getBookId());
        return ResponseEntity.ok(loanRecordDTO);
    }

    @PatchMapping("/return/{loanRecordId}")
    public ResponseEntity<LoanRecordDTO> returnBook(@PathVariable Long loanRecordId) {
        LoanRecordDTO loanRecordDTO = loanRecordService.returnBook(loanRecordId);
        return ResponseEntity.ok(loanRecordDTO);
    }

    @GetMapping
    public ResponseEntity<List<LoanRecordDTO>> getAllLoanRecords() {
        List<LoanRecordDTO> loanRecords = loanRecordService.getAllLoanRecords();
        return ResponseEntity.ok(loanRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRecordDTO> getLoanRecordById(@PathVariable Long id) {
        LoanRecordDTO loanRecordDTO = loanRecordService.getLoanRecordById(id);
        return ResponseEntity.ok(loanRecordDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanRecordDTO> updateLoanRecord(@PathVariable Long id, @RequestBody LoanRecordDTO loanRecordDTO) {
        LoanRecordDTO updatedLoanRecord = loanRecordService.updateLoanRecord(id, loanRecordDTO);
        return ResponseEntity.ok(updatedLoanRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoanRecord(@PathVariable Long id) {
        loanRecordService.deleteLoanRecord(id);
        return ResponseEntity.noContent().build();
    }
}
