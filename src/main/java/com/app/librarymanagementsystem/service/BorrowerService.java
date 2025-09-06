
// BorrowerService.java
package com.app.librarymanagementsystem.service;

import com.app.librarymanagementsystem.dto.BorrowerDTO;
import java.util.List;
import java.util.Optional;

public interface BorrowerService {
    BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO);
    void borrowBook(Long borrowerId, Long bookId);
    void returnBook(Long borrowerId, Long bookId);
    List<BorrowerDTO> getAllBorrowers();

    Optional<BorrowerDTO> getBorrowerById(Long borrowerId);
}