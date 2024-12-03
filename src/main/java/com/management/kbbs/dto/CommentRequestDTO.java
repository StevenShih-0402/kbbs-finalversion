package com.management.kbbs.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
    private Long userId;    // 使用者 ID
    private Long bookId;    // 書籍 ID
    private String content; // 評論內容
    private Integer rating; // 評分
}
