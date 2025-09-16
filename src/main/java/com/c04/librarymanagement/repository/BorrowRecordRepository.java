package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.BorrowRecord;
import com.c04.librarymanagement.model.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByDeletedFalse();
    List<BorrowRecord> findByDeletedFalse(Pageable pageable);
    List<BorrowRecord> findByStatus(BorrowStatus status);

    @Query("SELECT br FROM BorrowRecord br " +
            "WHERE br.deleted = false " +
            "AND (:status IS NULL OR br.status = :status) " +
            "AND (:keyword IS NULL OR LOWER(br.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(br.customer.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<BorrowRecord> search(@Param("status") BorrowStatus status,
                              @Param("keyword") String keyword,
                              Pageable pageable);
}
