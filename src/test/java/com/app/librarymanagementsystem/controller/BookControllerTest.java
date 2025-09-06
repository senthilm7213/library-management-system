package com.app.librarymanagementsystem.controller;

import com.app.librarymanagementsystem.dto.BookDTO;
import com.app.librarymanagementsystem.exception.ResourceNotFoundException;
import com.app.librarymanagementsystem.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp() {
        mockBookDTO = new BookDTO(1L, "1234567890", "Test Book", "Test Author", null);
    }

    @Test
    void testRegisterBook() {
        when(bookService.registerBook(any(BookDTO.class))).thenReturn(mockBookDTO);

        ResponseEntity<BookDTO> response = bookController.registerBook(mockBookDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockBookDTO, response.getBody());
    }

    @Test
    void testGetAllBooks() {
        List<BookDTO> mockBooks = Collections.singletonList(mockBookDTO);
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBooks, response.getBody());
    }
}
