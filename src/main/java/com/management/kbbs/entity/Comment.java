package com.management.kbbs.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 唯一識別每條評論

    @Column(nullable = false)
    private Long userId; // 使用者 ID

    @Column(nullable = false)
    private Long bookId; // 書籍 ID

    @Lob
    private String content; // 評論內容

    @Column(nullable = false)
    private Integer rating; // 評分（1 到 5）

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 評論時間
}

