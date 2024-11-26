package com.management.kbbs.service;

import com.management.kbbs.entity.Book;
import com.management.kbbs.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // 新增書籍
    public Book addBook(Book book) {
        if (book.getIsbn() != null && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN 已存在，無法新增書籍。");
        }
        return bookRepository.save(book);
    }

    // 查詢所有書籍
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // 根據 ID 查詢書籍
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到指定 ID 的書籍：" + id));
    }

    // 更新書籍
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id); // 確保書籍存在

        // 更新資料
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublishDate(updatedBook.getPublishDate());
        existingBook.setStock(updatedBook.getStock());

        return bookRepository.save(existingBook);
    }

    // 刪除書籍
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("找不到指定 ID 的書籍，無法刪除：" + id);
        }
        bookRepository.deleteById(id);
    }

    // 根據關鍵字查詢書籍 (書名包含特定字串)
    public List<Book> searchBooksByTitle(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }

    // 查詢存貨大於指定數量的書籍
    public List<Book> getBooksByStockGreaterThan(Integer stock) {
        return bookRepository.findByStockGreaterThan(stock);
    }
}