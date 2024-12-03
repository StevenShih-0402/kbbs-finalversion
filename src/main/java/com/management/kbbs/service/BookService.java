package com.management.kbbs.service;

import com.management.kbbs.dto.BookDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.repository.BookRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // 新增書籍
    public BookDTO createBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new IllegalArgumentException("書籍已存在 (ISBN 重複)。");
        }
        Book savedBook = bookRepository.save(setNewBook(bookDTO));
        return convertToDTO(savedBook);
    }

    // 查詢所有書籍
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
    }

    // 根據 ID 查詢書籍
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("未找到指定 ID 的書籍。"));
        return convertToDTO(book);
    }

    // 更新書籍
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                                          .orElseThrow(() -> new IllegalArgumentException("未找到指定 ID 的書籍。"));

        editBook(existingBook, bookDTO);

        Book updatedBook = bookRepository.save(existingBook);
        return convertToDTO(updatedBook);
    }

    // 刪除書籍
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("未找到指定 ID 的書籍。");
        }
        bookRepository.deleteById(id);
    }

    // 根據關鍵字查詢書籍 (書名包含特定字串)
    public List<BookDTO> searchBooksByTitle(String keyword) {
        return bookRepository.findByTitleContaining(keyword)
                             .stream()
                             .map(this::convertToDTO)
                             .collect(Collectors.toList());
    }








    // 將 Book 轉換為 BookDTO
    private BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();

        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPublishDate(book.getPublishDate());
        bookDTO.setCollection(book.getCollection());

        return bookDTO;
    }

    // 新增書籍的資料轉換
    private Book setNewBook(BookDTO bookDTO){
        Book book = new Book();
        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublishDate(bookDTO.getPublishDate());

        return book;
    }

    // 更新書籍的資料轉換
    private void editBook(Book existingBook, BookDTO bookDTO){
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublishDate(bookDTO.getPublishDate());
        existingBook.setCollection(bookDTO.getCollection());
    }
}
