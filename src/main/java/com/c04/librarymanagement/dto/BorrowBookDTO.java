package com.c04.librarymanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowBookDTO {
    private Long id;
    private String title;
    private String condition;
}

