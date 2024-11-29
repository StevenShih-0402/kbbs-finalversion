package com.management.kbbs.service;

import com.management.kbbs.dto.LoanRecordDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.LoanRecord;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.LoanRecordRepository;
import com.management.kbbs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanRecordService {

    private final LoanRecordRepository loanRecordRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // Create a new loan record
    public LoanRecordDTO borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        if ("館外".equals(book.getCollection())) {
            throw new RuntimeException("The book is already borrowed.");
        }

        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setUserId(userId);
        loanRecord.setBookId(bookId);
        loanRecord.setLoanDate(LocalDate.now());
        loanRecord.setDueDate(LocalDate.now().plusWeeks(3));
        loanRecord.setStatus("借閱中");

        book.setCollection("館外");
        bookRepository.save(book);

        LoanRecord savedLoanRecord = loanRecordRepository.save(loanRecord);
        return new LoanRecordDTO(savedLoanRecord.getId(), savedLoanRecord.getUserId(), savedLoanRecord.getBookId(),
                savedLoanRecord.getLoanDate(), savedLoanRecord.getDueDate(), savedLoanRecord.getReturnDate(), savedLoanRecord.getStatus());
    }

    // Return a book
    public LoanRecordDTO returnBook(Long loanRecordId) {
        LoanRecord loanRecord = loanRecordRepository.findById(loanRecordId)
                .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + loanRecordId));
        Book book = bookRepository.findById(loanRecord.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + loanRecord.getBookId()));

        loanRecord.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(loanRecord.getDueDate())) {
            loanRecord.setStatus("逾期歸還");
        } else {
            loanRecord.setStatus("已歸還");
        }

        book.setCollection("館內");
        bookRepository.save(book);

        LoanRecord updatedLoanRecord = loanRecordRepository.save(loanRecord);
        return new LoanRecordDTO(updatedLoanRecord.getId(), updatedLoanRecord.getUserId(), updatedLoanRecord.getBookId(),
                updatedLoanRecord.getLoanDate(), updatedLoanRecord.getDueDate(), updatedLoanRecord.getReturnDate(), updatedLoanRecord.getStatus());
    }

    // Get all loan records
    public List<LoanRecordDTO> getAllLoanRecords() {
        return loanRecordRepository.findAll().stream()
                .map(record -> new LoanRecordDTO(record.getId(), record.getUserId(), record.getBookId(),
                        record.getLoanDate(), record.getDueDate(), record.getReturnDate(), record.getStatus()))
                .collect(Collectors.toList());
    }

    // Get a loan record by ID
    public LoanRecordDTO getLoanRecordById(Long id) {
        LoanRecord loanRecord = loanRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));
        return new LoanRecordDTO(loanRecord.getId(), loanRecord.getUserId(), loanRecord.getBookId(),
                loanRecord.getLoanDate(), loanRecord.getDueDate(), loanRecord.getReturnDate(), loanRecord.getStatus());
    }

    // Update a loan record
    public LoanRecordDTO updateLoanRecord(Long id, LoanRecordDTO loanRecordDTO) {
        LoanRecord loanRecord = loanRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan record not found with ID: " + id));
        loanRecord.setUserId(loanRecordDTO.getUserId());
        loanRecord.setBookId(loanRecordDTO.getBookId());
        loanRecord.setLoanDate(loanRecordDTO.getLoanDate());
        loanRecord.setDueDate(loanRecordDTO.getDueDate());
        loanRecord.setReturnDate(loanRecordDTO.getReturnDate());
        loanRecord.setStatus(loanRecordDTO.getStatus());

        LoanRecord updatedLoanRecord = loanRecordRepository.save(loanRecord);
        return new LoanRecordDTO(updatedLoanRecord.getId(), updatedLoanRecord.getUserId(), updatedLoanRecord.getBookId(),
                updatedLoanRecord.getLoanDate(), updatedLoanRecord.getDueDate(), updatedLoanRecord.getReturnDate(), updatedLoanRecord.getStatus());
    }

    // Delete a loan record
    public void deleteLoanRecord(Long id) {
        if (!loanRecordRepository.existsById(id)) {
            throw new RuntimeException("Loan record not found with ID: " + id);
        }
        loanRecordRepository.deleteById(id);
    }
}
