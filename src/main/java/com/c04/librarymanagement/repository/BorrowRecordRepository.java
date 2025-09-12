package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByDeletedFalse();
    // Nếu muốn lọc theo trạng thái phiếu mượn
    // List<BorrowRecord> findByStatus(BorrowStatus status);
}
