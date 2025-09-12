package com.c04.librarymanagement.dto;

import com.c04.librarymanagement.model.BookCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookDTO {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Image cannot be null")
    private MultipartFile image;

    @NotNull(message = "Condition cannot be null")
    private BookCondition condition; // shared enum with Entity

    @NotNull(message = "Category cannot be null")
    private Long categoryId;

    @NotNull(message = "Publisher cannot be null")
    private Long publisherId;
}
