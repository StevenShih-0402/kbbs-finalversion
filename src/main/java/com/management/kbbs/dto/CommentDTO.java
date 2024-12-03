package com.management.kbbs.dto;

import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.User;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id; // 唯一識別每條評論
    private User user; // 使用者
    private Book book; // 書籍
    private String content; // 評論內容
    private Integer rating; // 評分（1 到 5）
    private LocalDateTime createdAt; // 評論時間
}
