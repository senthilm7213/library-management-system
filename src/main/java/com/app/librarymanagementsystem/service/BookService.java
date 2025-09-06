// BookService.java
package com.app.librarymanagementsystem.service;

import com.app.librarymanagementsystem.dto.BookDTO;
import java.util.List;

public interface BookService {
    BookDTO registerBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();

    BookDTO getBookById(Long bookId);
}
