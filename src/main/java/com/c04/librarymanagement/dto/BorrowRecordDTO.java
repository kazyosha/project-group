package com.c04.librarymanagement.dto;

import com.c04.librarymanagement.model.BorrowStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecordDTO {
    private Long id;
    private Long customerId;
    private String customerName; // để hiển thị
    private LocalDate borrowDate;
    private BorrowStatus status;

    private List<Long> bookIds;
    private List<BorrowDetailDTO> borrowDetails;
}
