package com.management.kbbs.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRecordRequestDTO {
    private Long userId;   // 使用者 ID
    private Long bookId;   // 書籍 ID
}
