package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.BorrowBookDTO;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private BorrowBookDTO toDTO(Book book) {
        return BorrowBookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .condition(book.getCondition() != null ? book.getCondition().name() : null)
                .build();
    }

    public List<BorrowBookDTO> getAllBooks() {
        return bookRepository.findByDeletedFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
