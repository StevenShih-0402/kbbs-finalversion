package com.management.kbbs;

import com.management.kbbs.controller.BookController;
import com.management.kbbs.dto.BookDTO;
import com.management.kbbs.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    private BookDTO bookDTO;

    private List<BookDTO> bookList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 初始化模擬資料
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");

        bookList = new ArrayList<>();
        bookList.add(bookDTO);
    }

    @Test
    void testCreateBook() {
        // Arrange
        when(bookService.createBook(any(BookDTO.class))).thenReturn(bookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.createBook(bookDTO);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(bookDTO, response.getBody());
        verify(bookService, times(1)).createBook(bookDTO);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        when(bookService.getAllBooks()).thenReturn(bookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookList, response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() {
        // Arrange
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(bookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookDTO, response.getBody());
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Long bookId = 1L;
        when(bookService.updateBook(eq(bookId), any(BookDTO.class))).thenReturn(bookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, bookDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookDTO, response.getBody());
        verify(bookService, times(1)).updateBook(bookId, bookDTO);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookService).deleteBook(bookId);

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void testSearchBooksByTitle() {
        // Arrange
        String keyword = "Test";
        when(bookService.searchBooksByTitle(keyword)).thenReturn(bookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.searchBooksByTitle(keyword);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookList, response.getBody());
        verify(bookService, times(1)).searchBooksByTitle(keyword);
    }
}