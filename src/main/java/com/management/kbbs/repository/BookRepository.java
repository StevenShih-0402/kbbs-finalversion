package com.management.kbbs.repository;

import com.management.kbbs.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 查詢書籍是否存在於資料庫 (根據 ISBN)
    boolean existsByIsbn(String isbn);

    // 根據書名進行模糊查詢
    Optional<Book> findByTitleContaining(String title);

//    @Query("SELECT b, COUNT(l.id) AS borrowCount " +
//            "FROM LoanRecords l JOIN l.book b " +
//            "GROUP BY b " +
//            "ORDER BY borrowCount DESC")
//    Optional<Object[]> findPopularBooks();
}