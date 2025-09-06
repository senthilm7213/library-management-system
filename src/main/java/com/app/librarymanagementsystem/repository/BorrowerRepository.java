// BorrowerRepository.java
package com.app.librarymanagementsystem.repository;

import com.app.librarymanagementsystem.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByEmail(String email);
}
