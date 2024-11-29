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
    private Integer stock;


    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public LocalDate getPublishDate() {
//        return publishDate;
//    }
//
//    public void setPublishDate(LocalDate publishDate) {
//        this.publishDate = publishDate;
//    }
//
//    public Integer getStock() {
//        return stock;
//    }
//
//    public void setStock(Integer stock) {
//        this.stock = stock;
//    }

//    @Override
//    public String toString() {
//        return "BookDTO{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", author='" + author + '\'' +
//                ", isbn='" + isbn + '\'' +
//                ", publishDate=" + publishDate +
//                ", stock=" + stock +
//                '}';
//    }
}

