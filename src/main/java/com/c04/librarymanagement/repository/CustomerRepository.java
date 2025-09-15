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
    @Query("SELECT c FROM Customer c WHERE c.deleted = false AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Customer> searchByNameOrCode(@Param("keyword") String keyword, Pageable pageable);
    List<Customer> findTop10ByDeletedFalseAndNameContainingIgnoreCaseOrDeletedFalseAndCodeContainingIgnoreCase(
            String name, String code);
}
