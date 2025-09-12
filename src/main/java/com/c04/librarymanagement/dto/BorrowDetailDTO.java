package com.c04.librarymanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowDetailDTO {
    private Long bookId;
    private String bookTitle;
}