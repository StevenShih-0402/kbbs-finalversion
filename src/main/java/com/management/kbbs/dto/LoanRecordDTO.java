package com.management.kbbs.dto;

import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.User;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecordDTO {

    private Long id;          // 借閱記錄 ID
    private User user;      // 使用者
    private Book book;      // 書籍
    private LocalDate loanDate;    // 借閱日期
    private LocalDate dueDate;     // 應還日期
    private LocalDate returnDate;  // 實際歸還日期
    private String status;    // 借閱狀態
}