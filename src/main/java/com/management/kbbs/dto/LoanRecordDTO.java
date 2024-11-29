package com.management.kbbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecordDTO {

    private Long id;          // 借閱記錄 ID
    private Long userId;      // 使用者 ID
    private Long bookId;      // 書籍 ID
    private Date loanDate;    // 借閱日期
    private Date dueDate;     // 應還日期
    private Date returnDate;  // 實際歸還日期
    private String status;    // 借閱狀態

    // 其他轉換方法根據需求添加，例如 fromEntity 和 toEntity
}