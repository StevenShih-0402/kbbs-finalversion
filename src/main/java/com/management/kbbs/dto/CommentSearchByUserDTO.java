package com.management.kbbs.dto;
// 用 Book / User 的 id 查詢評論用的 DTO

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchByUserDTO {
    private Long id;            // 評論 ID
    private String bookTitle;   // 書籍標題
    private String bookAuthor;  // 書籍作者
    private String content;     // 評論內容
    private Integer rating;     // 評分（1 到 5）
    private LocalDateTime createdAt; // 評論時間
}
