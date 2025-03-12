package com.management.kbbs.dto;
// 用於列出書籍平均評分和評論數的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEvaluateDTO {
    private Long id;
    private String title;
    private Double averageRating;
    private Long commentCount;
}
