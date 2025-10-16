package com.c04.librarymanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibrarianDTO {
    private Long id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

//    @Size(min = 6, message = "Mật khẩu ít nhất 6 ký tự")
//    private String password; // có thể trống nếu không đổi

    private String imageUrl; // avatar hiện tại
    private MultipartFile avatarFile; // file upload mới

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng 0 và có 10-11 số")
    private String phone;
}
