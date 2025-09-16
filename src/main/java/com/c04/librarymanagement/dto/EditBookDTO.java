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

    @NotNull(message = "ID sách không được để trống")
    private Long id;

    @NotBlank(message = "Tên sách không được để trống")
    @Size(max = 255, message = "Tên sách tối đa 255 ký tự")
    private String title;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    private MultipartFile imageFile;
    private String currentImage;

    @NotNull(message = "Tình trạng không được để trống")
    private BookCondition condition;

    @NotNull(message = "Thể loại không được để trống")
    private Long categoryId;

    @NotNull(message = "Nhà xuất bản không được để trống")
    private Long publisherId;
}
