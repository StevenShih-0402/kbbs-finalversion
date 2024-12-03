package com.management.kbbs.dto;
// 用來查詢活躍用戶的 DTO

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDTO {
    private String userName;
    private Long loanCount;  // 借閱次數
}
