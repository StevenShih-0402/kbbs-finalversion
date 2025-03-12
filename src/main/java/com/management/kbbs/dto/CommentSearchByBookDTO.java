package com.management.kbbs.dto;
// 用 Book / User 的 id 查詢評論用的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchByBookDTO {
    private Long id;            // 評論 ID
    private String userName;    // 使用者姓名
    private String content;     // 評論內容
    private Integer rating;     // 評分（1 到 5）
    private LocalDateTime createdAt; // 評論時間
}