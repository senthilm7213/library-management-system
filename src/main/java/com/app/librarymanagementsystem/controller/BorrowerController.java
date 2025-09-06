package com.app.librarymanagementsystem.controller;

import com.app.librarymanagementsystem.dto.BorrowerDTO;
import com.app.librarymanagementsystem.service.BorrowerService;
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
@RequestMapping("/api/borrowers")
@Slf4j
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(@Valid @RequestBody BorrowerDTO borrowerDTO) {
        log.info("Request to create borrower: {}", borrowerDTO);

        BorrowerDTO savedBorrower = borrowerService.registerBorrower(borrowerDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBorrower.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedBorrower);
    }

    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to borrow book borrowerId={}, bookId={}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        log.info("Request to return book borrowerId={}, bookId={}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        log.info("Request to get all borrowers");
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDTO> getBorrowerDetails(@PathVariable Long borrowerId) {
        log.info("Request to get borrower with id={}", borrowerId);

        return borrowerService.getBorrowerById(borrowerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
