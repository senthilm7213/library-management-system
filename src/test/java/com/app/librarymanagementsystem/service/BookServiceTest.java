package com.app.librarymanagementsystem.service;

import com.app.librarymanagementsystem.dto.BookDTO;
import com.app.librarymanagementsystem.entity.Book;
import com.app.librarymanagementsystem.entity.Borrower;
import com.app.librarymanagementsystem.exception.ResourceNotFoundException;
import com.app.librarymanagementsystem.repository.BookRepository;
import com.app.librarymanagementsystem.repository.BorrowerRepository;
import com.app.librarymanagementsystem.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDTO testBookDTO;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO(null, "1234567890", "Test Book", "Test Author", null);
        testBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
    }

    @Test
    void testRegisterBook() {
        mockModelMapper(testBookDTO, testBook);

        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDTO savedBook = bookService.registerBook(testBookDTO);

        assertEquals(testBookDTO.getIsbn(), savedBook.getIsbn());
        assertEquals(testBookDTO.getTitle(), savedBook.getTitle());
        assertEquals(testBookDTO.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testRegisterBookWithExistingISBNConflict() {
        Book conflictingBook = new Book(2L, "1234567890", "Different Title", "Different Author", null);
        when(bookRepository.findByIsbn(testBookDTO.getIsbn())).thenReturn(List.of(conflictingBook));

        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(testBookDTO));
    }

    @Test
    void testRegisterBookWithValidBorrowerId() {
        Borrower borrower = new Borrower(1L, "doe@john.com", "John Doe");
        testBookDTO.setBorrowerId(1L);

        mockModelMapper(testBookDTO, testBook);
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDTO savedBook = bookService.registerBook(testBookDTO);

        assertNotNull(savedBook);
        assertEquals(testBookDTO.getBorrowerId(), savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithInvalidBorrowerId() {
        testBookDTO.setBorrowerId(999L);

        mockModelMapper(testBookDTO, testBook);
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.registerBook(testBookDTO));
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book(1L, "1234567890", "Book 1", "Author 1", null);
        Book book2 = new Book(2L, "0987654321", "Book 2", "Author 2", null);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);
        when(modelMapper.map(book1, BookDTO.class)).thenReturn(new BookDTO(book1.getId(), book1.getIsbn(), book1.getTitle(), book1.getAuthor(), null));
        when(modelMapper.map(book2, BookDTO.class)).thenReturn(new BookDTO(book2.getId(), book2.getIsbn(), book2.getTitle(), book2.getAuthor(), null));

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(2, result.size());
    }

    @Test
    void testGetBookByIdSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(modelMapper.map(testBook, BookDTO.class)).thenReturn(testBookDTO);

        Optional<BookDTO> bookDTO = bookService.getBookById(1L);

        assertEquals(testBookDTO.getTitle(), bookDTO.get().getTitle());
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(99L));
    }

    // Helper method for mocking ModelMapper
    private void mockModelMapper(BookDTO dto, Book book) {
        when(modelMapper.map(dto, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(dto);
    }
}
