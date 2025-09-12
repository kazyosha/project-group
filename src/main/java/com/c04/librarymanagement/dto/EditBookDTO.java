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
@Builder
public class EditBookDTO {
    @NotNull(message = "{book.id.notnull}")
    private Long id;

    @NotBlank(message = "{book.title.notblank}")
    @Size(max = 255, message = "{book.title.size}")
    private String title;

    @Size(max = 1000, message = "{book.description.size}")
    private String description;

    // Cho phép upload ảnh mới, không bắt buộc
    private MultipartFile imageFile;

    private String currentImage; // để hiển thị ảnh cũ

    @NotNull(message = "{book.condition.notnull}")
    private BookCondition condition;

    @NotNull(message = "{book.category.notnull}")
    private Long categoryId;

    @NotNull(message = "{book.publisher.notnull}")
    private Long publisherId;
}
