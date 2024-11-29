package com.management.kbbs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "LOANRECORDS")
@AllArgsConstructor
@NoArgsConstructor
public class LoanRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 對應 Users 表的 id

    @Column(name = "book_id", nullable = false)
    private Long bookId; // 對應 Books 表的 id

    @Temporal(TemporalType.DATE)
    @Column(name = "loan_date", nullable = false, updatable = false)
    private LocalDate loanDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "status", length = 50, nullable = false)
    private String status = "借閱中";
}
