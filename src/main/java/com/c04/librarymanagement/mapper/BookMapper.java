package com.c04.librarymanagement.mapper;

import com.c04.librarymanagement.dto.CreateBookDTO;
import com.c04.librarymanagement.dto.EditBookDTO;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.model.Category;
import com.c04.librarymanagement.model.Publisher;

public class BookMapper {

    public static Book toEntity(CreateBookDTO dto, String imagePath, Category category, Publisher publisher) {
        return Book.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .image(imagePath)
                .condition(dto.getCondition())
                .category(category)
                .publisher(publisher)
                .build();
    }

    public static EditBookDTO toEditBookDTO(Book book) {
        return EditBookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .currentImage(book.getImage())
                .condition(book.getCondition())
                .categoryId(book.getCategory() != null ? book.getCategory().getId() : null)
                .publisherId(book.getPublisher() != null ? book.getPublisher().getId() : null)
                .build();
    }
}
