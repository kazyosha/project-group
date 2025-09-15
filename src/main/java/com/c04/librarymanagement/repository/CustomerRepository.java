package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByDeletedFalse(Pageable pageable);
    Page<Customer> findByDeletedTrue(Pageable pageable);
    List<Customer> findByDeletedTrue();
    Page<Customer> findByDeletedFalseAndNameContainingIgnoreCaseOrDeletedFalseAndCodeContainingIgnoreCase(
            String name, String code, Pageable pageable);
}
