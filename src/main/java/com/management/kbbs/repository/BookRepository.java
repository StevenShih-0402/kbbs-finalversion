package com.management.kbbs.repository;

import com.management.kbbs.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 查詢書籍是否存在於資料庫 (根據 ISBN)
    boolean existsByIsbn(String isbn);

    // 根據書名進行模糊查詢
    List<Book> findByTitleContaining(String title);

    // 查詢存貨大於指定數量的書籍
    List<Book> findByStockGreaterThan(Integer stock);

    // 使用自定義 SQL 進行 JOIN 查詢 (範例)
    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.stock > :stock")
    List<Book> findBooksWithAuthorAndStockGreaterThan(Integer stock);
}
