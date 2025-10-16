package com.c04.librarymanagement.dto;

import com.c04.librarymanagement.model.BorrowStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Vui lòng chọn sinh viên mượn sách.")
    private Long customerId;

    private String customerName;

    @NotNull(message = "Ngày mượn không được để trống.")
    private LocalDate borrowDate;

    @NotNull(message = "Trạng thái mượn không được để trống.")
    private BorrowStatus status;

    @NotEmpty(message = "Vui lòng chọn ít nhất một cuốn sách.")
    private List<Long> bookIds;

    private List<BorrowDetailDTO> borrowDetails;
}
