// BookService.java
package com.app.librarymanagementsystem.service;

import com.app.librarymanagementsystem.dto.BookDTO;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDTO registerBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();

    Optional<BookDTO> getBookById(Long bookId);
}
