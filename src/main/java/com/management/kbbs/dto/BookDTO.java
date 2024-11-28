package com.management.kbbs.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publishDate;
    private Integer stock;

    // Default constructor
    public BookDTO() {
    }

    // Constructor with parameters
    public BookDTO(Long id, String title, String author, String isbn, LocalDate publishDate, Integer stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.stock = stock;
    }

    // Factory method to convert Entity to DTO
    public static BookDTO fromEntity(com.management.kbbs.entity.Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishDate(),
                book.getStock()
        );
    }

    // Method to convert DTO back to Entity
    public com.management.kbbs.entity.Book toEntity() {
        return new com.management.kbbs.entity.Book(
                this.title,
                this.author,
                this.isbn,
                this.publishDate,
                this.stock
        );
    }

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

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publishDate=" + publishDate +
                ", stock=" + stock +
                '}';
    }
}

