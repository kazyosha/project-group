package com.c04.librarymanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate borrowDate;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    // Quan hệ n-1 với Customer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Quan hệ 1-n với BorrowDetail
    @OneToMany(mappedBy = "borrowRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowDetail> borrowDetails;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
