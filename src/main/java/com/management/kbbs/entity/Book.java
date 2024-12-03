package com.management.kbbs.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    private LocalDate publishDate;

    @Column(nullable = false)
    private String collection = "館內"; // 館藏狀態，預設值為 "館內"
}