package com.management.kbbs.dto;
// 新增借閱紀錄用的 DTO

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
