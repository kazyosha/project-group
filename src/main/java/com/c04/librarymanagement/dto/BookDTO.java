package com.c04.librarymanagement.dto;

import com.c04.librarymanagement.model.BookCondition;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String description;
    private String image;
    private BookCondition condition;
    private Long categoryId;
    private String categoryName;
    private Long publisherId;
    private String publisherName;
}
