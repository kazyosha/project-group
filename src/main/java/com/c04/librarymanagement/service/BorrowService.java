package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.BorrowRecordDTO;
import com.c04.librarymanagement.dto.BorrowDetailDTO;
import com.c04.librarymanagement.model.*;
import com.c04.librarymanagement.repository.BorrowRecordRepository;
import com.c04.librarymanagement.repository.BookRepository;
import com.c04.librarymanagement.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         CustomerRepository customerRepository,
                         BookRepository bookRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
    }

    // Chuyển từ DTO sang entity
    public BorrowRecord toEntity(BorrowRecordDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        BorrowRecord record = new BorrowRecord();
        record.setCustomer(customer);
        record.setBorrowDate(LocalDate.now());
        record.setStatus(BorrowStatus.BORROWING);
        record.setDeleted(false);


        List<BorrowDetail> details = dto.getBookIds().stream()
                .map(bookId -> {
                    Book book = bookRepository.findById(bookId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sách id=" + bookId));
                    return BorrowDetail.builder()
                            .borrowRecord(record)
                            .book(book)
                            .build();
                }).collect(Collectors.toList());

        record.setBorrowDetails(details);
        return record;
    }

    // Chuyển từ entity sang DTO
    public BorrowRecordDTO toDTO(BorrowRecord record) {
        List<BorrowDetailDTO> details = record.getBorrowDetails().stream()
                .map(d -> BorrowDetailDTO.builder()
                        .bookId(d.getBook().getId())
                        .bookTitle(d.getBook().getTitle())
                        .build())
                .collect(Collectors.toList());

        return BorrowRecordDTO.builder()
                .id(record.getId())
                .customerId(record.getCustomer().getId())
                .customerName(record.getCustomer().getName())
                .borrowDate(record.getBorrowDate())
                .status(record.getStatus())
                .borrowDetails(details)
                .build();
    }

    // Tạo phiếu mượn
    public BorrowRecordDTO createBorrowRecord(BorrowRecordDTO dto) {
        BorrowRecord record = toEntity(dto);
        BorrowRecord saved = borrowRecordRepository.save(record);
        return toDTO(saved);
    }

    public List<BorrowRecordDTO> getAllBorrowRecords() {
        return borrowRecordRepository.findAll().stream().map(record ->
                BorrowRecordDTO.builder()
                        .id(record.getId())
                        .customerId(record.getCustomer().getId())
                        .customerName(record.getCustomer().getName())
                        .borrowDate(record.getBorrowDate())
                        .status(record.getStatus())
                        .borrowDetails(record.getBorrowDetails().stream()
                                .map(d -> BorrowDetailDTO.builder()
                                        .bookId(d.getBook().getId())
                                        .bookTitle(d.getBook().getTitle())
                                        .build())
                                .toList())
                        .build()
        ).toList();
    }

    public BorrowRecordDTO updateStatus(Long recordId, BorrowStatus status) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn"));

        record.setStatus(status);
        BorrowRecord saved = borrowRecordRepository.save(record);

        return BorrowRecordDTO.builder()
                .id(saved.getId())
                .status(saved.getStatus())
                .customerId(saved.getCustomer().getId())
                .customerName(saved.getCustomer().getName())
                .borrowDate(saved.getBorrowDate())
                .borrowDetails(saved.getBorrowDetails().stream()
                        .map(d -> BorrowDetailDTO.builder()
                                .bookId(d.getBook().getId())
                                .bookTitle(d.getBook().getTitle())
                                .build())
                        .toList())
                .build();
    }

    public Page<BorrowRecordDTO> searchBorrowRecords(BorrowStatus status, String keyword, Pageable pageable) {
        Page<BorrowRecord> page = borrowRecordRepository.search(status, keyword, pageable);
        return page.map(this::toDTO);
    }

}
