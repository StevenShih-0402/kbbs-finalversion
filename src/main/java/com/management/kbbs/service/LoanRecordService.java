package com.management.kbbs.service;

import com.management.kbbs.dto.*;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.LoanRecord;
import com.management.kbbs.entity.User;
import com.management.kbbs.exception.GlobalExceptionHandler;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.LoanRecordRepository;
import com.management.kbbs.repository.UserRepository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanRecordService {

    private final LoanRecordRepository loanRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private final int MAX_LOANS_PER_USER = 6;

    // 新增借閱紀錄(借書)
    @Transactional
    public String borrowBook(Long bookId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByName(username)
                                  .orElseThrow(() -> new RuntimeException("User not found with User: " + username));
        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        // 檢查書籍館藏狀態
        if ("館外".equals(book.getCollection())) {
            throw new RuntimeException("The book is already borrowed.");
        }

        // 檢查當前借閱數量
        int activeLoans = loanRecordRepository.countActiveLoansByUserId(user.getId());
        if (activeLoans >= MAX_LOANS_PER_USER) {
            throw new GlobalExceptionHandler.BorrowLimitException("User has reached the maximum loan limit of " + MAX_LOANS_PER_USER);
        }

        bookIO(book, "館外");

        LoanRecord savedLoanRecord = loanRecordRepository.save(setNewLoadRecord(user, book));
//        return convertToDTO(savedLoanRecord);
        return "借閱者 " + savedLoanRecord.getUser().getName()  + " 已完成 《" + savedLoanRecord.getBook().getTitle() + "》 的借閱！請於 " + LocalDate.now().plusWeeks(3) + " 前完成歸還！";
    }

    // 更新借閱紀錄(還書)
    @Transactional
    public String returnBook(Long id) {
        LoanRecord loanRecord = loanRecordRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));

        updateReturnLoadRecord(loanRecord);
        bookIO(loanRecord.getBook(), "館內");

        LoanRecord updatedLoanRecord = loanRecordRepository.save(loanRecord);
//        return convertToDTO(updatedLoanRecord);
        return "借閱者 " + updatedLoanRecord.getUser().getName() + " 已完成 《" + updatedLoanRecord.getBook().getTitle() + "》 的歸還！謝謝您！";
    }

    // 查詢所有借閱紀錄
    public List<LoanRecordDTO> getAllLoanRecords() {
        return loanRecordRepository.findAll()
                                   .stream()
                                   .map(this::convertToDTO)
                                   .collect(Collectors.toList());
    }

    // 查詢單一特定借閱紀錄
    public LoanRecordDTO getLoanRecordById(Long id) {
        LoanRecord loanRecord = loanRecordRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));
        return convertToDTO(loanRecord);
    }

    // 更新借閱紀錄
    public LoanRecordDTO updateLoanRecord(Long id, LoanRecordUpdateDTO updateDTO) {
        LoanRecord existLoanRecord = loanRecordRepository.findById(id)
                                                         .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));

        editLoadRecord(existLoanRecord, updateDTO);

        LoanRecord updatedLoanRecord = loanRecordRepository.save(existLoanRecord);
        return convertToDTO(updatedLoanRecord);
    }

    // 刪除借閱紀錄
    public void deleteLoanRecord(Long id) {
        if (!loanRecordRepository.existsById(id)) {
            throw new RuntimeException("Loan record not found with ID: " + id);
        }
        loanRecordRepository.deleteById(id);
    }

    // 熱門書籍排行(列出借閱次數最多的書籍)
    public List<BookPopularDTO> getPopularBooks(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return loanRecordRepository.findPopularBooks(pageable);
    }

    // 活躍用戶排行(列出借閱次數最多的用戶)
    public List<UserActivityDTO> getActiveUsers(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return loanRecordRepository.findActiveUsers(pageable);
    }

    // 列出未歸還的書籍清單
    public List<BookUnreturnDTO> getUnreturnedBooks() {
        return loanRecordRepository.findUnreturnedBooks();
    }

    // 讓使用者查詢簡易的借閱紀錄
    public List<UserSimpleLoanDTO> getSimplifiedLoanRecords() {
        // 獲取當前登入使用者的名稱
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 調用 Repository 層來查詢該使用者的借閱紀錄
        return loanRecordRepository.findSimplifiedLoanRecordsByUserName(username);
    }





    // 將 LoanRecord 轉換為 LoanRecordDTO
    private LoanRecordDTO convertToDTO(LoanRecord loanrecord){
        LoanRecordDTO loanRecordDTO = new LoanRecordDTO();

        loanRecordDTO.setId(loanrecord.getId());
        loanRecordDTO.setLoanDate(loanrecord.getLoanDate());
        loanRecordDTO.setDueDate(loanrecord.getDueDate());
        loanRecordDTO.setReturnDate(loanrecord.getReturnDate());
        loanRecordDTO.setStatus(loanrecord.getStatus());
        loanRecordDTO.setUser(loanrecord.getUser());
        loanRecordDTO.setBook(loanrecord.getBook());

        return loanRecordDTO;
    }

    // 借書的資料轉換
    private LoanRecord setNewLoadRecord(User user, Book book){
        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setLoanDate(LocalDate.now());
        loanRecord.setDueDate(LocalDate.now().plusWeeks(3));
        loanRecord.setStatus("借閱中");
        loanRecord.setUser(user);
        loanRecord.setBook(book);

        return loanRecord;
    }

    // 還書的資料轉換
    private void updateReturnLoadRecord(LoanRecord loanRecord){
        loanRecord.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(loanRecord.getDueDate())) {
            loanRecord.setStatus("逾期歸還");
        } else {
            loanRecord.setStatus("已歸還");
        }
    }

    // 更新借閱紀錄的資料轉換
    private void editLoadRecord(LoanRecord existLoanRecord, LoanRecordUpdateDTO updateDTO){

        existLoanRecord.setLoanDate(updateDTO.getLoanDate());
        existLoanRecord.setDueDate(updateDTO.getDueDate());
        existLoanRecord.setReturnDate(updateDTO.getReturnDate());
        existLoanRecord.setStatus(updateDTO.getStatus());
    }

    // 變更館藏狀態
    private void bookIO(Book book, String collectionStatus){
        book.setCollection(collectionStatus);
        bookRepository.save(book);
    }
}
