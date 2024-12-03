package com.management.kbbs.dto;

import com.management.kbbs.entity.Book;
import lombok.Data;

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