package com.management.kbbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentKafkaDTO {
    private Long userId;
    private Long bookId;
    private String content;
    private Integer rating;
    private LocalDateTime createdAt;
}
