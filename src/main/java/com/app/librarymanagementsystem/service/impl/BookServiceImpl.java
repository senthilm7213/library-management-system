// BookServiceImpl.java
package com.app.librarymanagementsystem.service.impl;

import com.app.librarymanagementsystem.dto.BookDTO;
import com.app.librarymanagementsystem.entity.Book;
import com.app.librarymanagementsystem.entity.Borrower;
import com.app.librarymanagementsystem.exception.ResourceNotFoundException;
import com.app.librarymanagementsystem.repository.BookRepository;
import com.app.librarymanagementsystem.repository.BorrowerRepository;
import com.app.librarymanagementsystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final BorrowerRepository borrowerRepository;

    @Override
    public BookDTO registerBook(BookDTO bookDTO) {
        validateBook(bookDTO);

        Book book = modelMapper.map(bookDTO, Book.class);

        // Associate borrower if provided
        if (bookDTO.getBorrowerId() != null && bookDTO.getBorrowerId() > 0) {
            Borrower borrower = borrowerRepository.findById(bookDTO.getBorrowerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + bookDTO.getBorrowerId()));
            book.setBorrower(borrower);
        } else {
            book.setBorrower(null);
        }

        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDTO.class);
    }

    private void validateBook(BookDTO bookDTO) {
        List<Book> existingBooks = bookRepository.findByIsbn(bookDTO.getIsbn());
        existingBooks.forEach(existingBook -> {
            if (!existingBook.getTitle().equals(bookDTO.getTitle()) ||
                    !existingBook.getAuthor().equals(bookDTO.getAuthor())) {
                throw new IllegalArgumentException(
                        "Books with the same ISBN must have the same title and author");
            }
        });
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .toList();
    }

    @Override
    public Optional<BookDTO> getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        return Optional.ofNullable(modelMapper.map(book, BookDTO.class));
    }
}
