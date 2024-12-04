package com.management.kbbs.dto;
// 用於列出未歸還書籍的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUnreturnDTO {
    private Long loanRecordId;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String borrowerName;
    private LocalDate loanDate;
    private LocalDate dueDate;
}
