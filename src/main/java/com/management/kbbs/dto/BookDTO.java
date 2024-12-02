package com.management.kbbs.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishDate;
    private String collection;
}