package com.management.kbbs.service;

import com.management.kbbs.dto.BookDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Create a new book
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookDTO.toEntity(); // Convert DTO to Entity
        Book savedBook = bookRepository.save(book); // Save to the database
        return BookDTO.fromEntity(savedBook); // Convert saved Entity back to DTO
    }

    // Get all books
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll(); // Fetch all books from the database
        return books.stream()
                .map(BookDTO::fromEntity) // Convert each Entity to DTO
                .collect(Collectors.toList());
    }

    // Get a book by its ID
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id)); // Handle not found
        return BookDTO.fromEntity(book); // Convert to DTO
    }

    // Update a book
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        // Find the book to update
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));

        // Update fields from DTO
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublishDate(bookDTO.getPublishDate());
        existingBook.setStock(bookDTO.getStock());

        Book updatedBook = bookRepository.save(existingBook); // Save updated book
        return BookDTO.fromEntity(updatedBook); // Convert to DTO
    }

    // Delete a book
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with ID: " + id); // Handle not found
        }
        bookRepository.deleteById(id); // Delete the book
    }
}
