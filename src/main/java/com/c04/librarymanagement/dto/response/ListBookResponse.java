package com.c04.librarymanagement.dto.response;
import com.c04.librarymanagement.dto.BookDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListBookResponse {
    private List<BookDTO> books;
    private int currentPage;
    private int totalPage;
}