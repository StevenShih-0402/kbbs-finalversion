package com.management.kbbs.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 使用 Lombok 簡化 Getter/Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanRecordRequestDTO {
    private Long userId;
    private Long bookId;
}