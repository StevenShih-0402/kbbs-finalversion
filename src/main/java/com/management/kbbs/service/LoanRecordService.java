package com.management.kbbs.service;

import com.management.kbbs.dto.LoanRecordDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.LoanRecord;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.LoanRecordRepository;
import com.management.kbbs.repository.UserRepository;

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
    public LoanRecordDTO borrowBook(LoanRecordDTO loanRecordDTO) {
        User user = userRepository.findById(loanRecordDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + loanRecordDTO.getUser().getId()));
        Book book = bookRepository.findById(loanRecordDTO.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + loanRecordDTO.getBook().getId()));

        if ("館外".equals(book.getCollection())) {
            throw new RuntimeException("The book is already borrowed.");
        }

        bookIO(book, "館外");

        LoanRecord savedLoanRecord = loanRecordRepository.save(setNewLoadRecord(loanRecordDTO));
        return convertToDTO(savedLoanRecord);
    }

    // 更新借閱紀錄(還書)
    public LoanRecordDTO returnBook(LoanRecordDTO loanRecordDTO) {
        LoanRecord loanRecord = loanRecordRepository.findById(loanRecordDTO.getId())
                .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + loanRecordDTO.getId()));
        Book book = bookRepository.findById(loanRecordDTO.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + loanRecordDTO.getBook().getId()));

        updateReturnLoadRecord(loanRecord);
        bookIO(book, "館內");

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
    public LoanRecordDTO updateLoanRecord(Long id, LoanRecordDTO loanRecordDTO) {
        LoanRecord existLoanRecord = loanRecordRepository.findById(id)
                                                        .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));

        editLoadRecord(existLoanRecord, loanRecordDTO);

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
    private LoanRecord setNewLoadRecord(LoanRecordDTO loanRecordDTO){
        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setUser(loanRecordDTO.getUser());
        loanRecord.setBook(loanRecordDTO.getBook());
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
    private void editLoadRecord(LoanRecord existLoanRecord, LoanRecordDTO loanRecordDTO){
        existLoanRecord.setUser(loanRecordDTO.getUser());
        existLoanRecord.setBook(loanRecordDTO.getBook());
        existLoanRecord.setLoanDate(loanRecordDTO.getLoanDate());
        existLoanRecord.setDueDate(loanRecordDTO.getDueDate());
        existLoanRecord.setReturnDate(loanRecordDTO.getReturnDate());
        existLoanRecord.setStatus(loanRecordDTO.getStatus());
    }

    // 變更館藏狀態
    private void bookIO(Book book, String collectionStatus){
        book.setCollection(collectionStatus);
        bookRepository.save(book);
    }
}
