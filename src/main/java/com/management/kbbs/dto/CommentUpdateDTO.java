package com.management.kbbs.dto;
// 更新評論用的 DTO

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDTO {
    private String content; // 評論內容
    private Integer rating; // 評分（1 到 5）
}
