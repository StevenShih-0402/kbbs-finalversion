package com.management.kbbs.dto;
// 建立評論用的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    private Long bookId;    // 書籍 ID
    private String content; // 評論內容
    private Integer rating; // 評分
}
