package com.management.kbbs.dto;
// 用於查詢熱門書籍的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookPopularDTO {
    private Long bookId;
    private String title;
    private String author;
    private Long borrowCount;
}
