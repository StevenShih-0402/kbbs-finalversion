package com.management.kbbs.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id; // 唯一識別每條評論
    private Long userId; // 使用者 ID
    private Long bookId; // 書籍 ID
    private String content; // 評論內容
    private Integer rating; // 評分（1 到 5）
    private LocalDateTime createdAt; // 評論時間
}
