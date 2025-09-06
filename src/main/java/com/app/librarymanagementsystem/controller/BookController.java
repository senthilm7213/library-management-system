package com.app.librarymanagementsystem.controller;

import com.app.librarymanagementsystem.dto.BookDTO;
import com.app.librarymanagementsystem.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> registerBook(@Valid @RequestBody BookDTO bookDTO) {
        log.info("Request to create book: {}", bookDTO);

        BookDTO savedBook = bookService.registerBook(bookDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBook.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedBook);
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("Request to get all books");
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookDetails(@PathVariable Long bookId) {
        log.info("Request to get book with id: {}", bookId);

        return bookService.getBookById(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
