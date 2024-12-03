package com.management.kbbs.service;

import com.management.kbbs.dto.LoanRecordDTO;
import com.management.kbbs.dto.LoanRecordRequestDTO;
import com.management.kbbs.dto.LoanRecordUpdateDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.LoanRecord;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.LoanRecordRepository;
import com.management.kbbs.repository.UserRepository;
import com.management.kbbs.dto.PopularBookDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanRecordService {

    private final LoanRecordRepository loanRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 新增借閱紀錄(借書)
    public LoanRecordDTO borrowBook(LoanRecordRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                                  .orElseThrow(() -> new RuntimeException("User not found with ID: " + requestDTO.getUserId()));
        Book book = bookRepository.findById(requestDTO.getBookId())
                                  .orElseThrow(() -> new RuntimeException("Book not found with ID: " + requestDTO.getBookId()));

        if ("館外".equals(book.getCollection())) {
            throw new RuntimeException("The book is already borrowed.");
        }

        bookIO(book, "館外");

        LoanRecord savedLoanRecord = loanRecordRepository.save(setNewLoadRecord(user, book));
        return convertToDTO(savedLoanRecord);
    }

    // 更新借閱紀錄(還書)
    public LoanRecordDTO returnBook(Long id) {
        LoanRecord loanRecord = loanRecordRepository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));

        updateReturnLoadRecord(loanRecord);
        bookIO(loanRecord.getBook(), "館內");

        LoanRecord updatedLoanRecord = loanRecordRepository.save(loanRecord);
        return convertToDTO(updatedLoanRecord);
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
    public List<PopularBookDTO> getPopularBooks(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return loanRecordRepository.findPopularBooks(pageable);
    }




    // 將 LoanRecord 轉換為 LoanRecordDTO
    private LoanRecordDTO convertToDTO(LoanRecord loanrecord){
        LoanRecordDTO loanRecordDTO = new LoanRecordDTO();

        loanRecordDTO.setId(loanrecord.getId());
        loanRecordDTO.setUser(loanrecord.getUser());
        loanRecordDTO.setBook(loanrecord.getBook());
        loanRecordDTO.setLoanDate(loanrecord.getLoanDate());
        loanRecordDTO.setDueDate(loanrecord.getDueDate());
        loanRecordDTO.setReturnDate(loanrecord.getReturnDate());
        loanRecordDTO.setStatus(loanrecord.getStatus());

        return loanRecordDTO;
    }

    // 借書的資料轉換
    private LoanRecord setNewLoadRecord(User user, Book book){
        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setUser(user);
        loanRecord.setBook(book);
        loanRecord.setLoanDate(LocalDate.now());
        loanRecord.setDueDate(LocalDate.now().plusWeeks(3));
        loanRecord.setStatus("借閱中");

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
