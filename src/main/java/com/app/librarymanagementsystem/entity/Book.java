// Book.java
package com.app.librarymanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ISBN cannot be blank")
    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 150, message = "Title must be between 1 and 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;
}
