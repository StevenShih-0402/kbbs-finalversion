package com.management.kbbs.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecordUpdateDTO {
    private LocalDate loanDate;    // 借閱日期
    private LocalDate dueDate;     // 應還日期
    private LocalDate returnDate;  // 還書日期
    private String status;         // 借閱狀態
}
