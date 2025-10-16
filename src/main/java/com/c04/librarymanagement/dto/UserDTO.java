package com.c04.librarymanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu ít nhất 6 ký tự")
    private String password;

    private String imageUrl;

    private MultipartFile imageUrlFile;

//    @NotNull(message = "Ngày sinh không được để trống")
//    @Past(message = "Ngày sinh phải ở quá khứ")
    private LocalDate birthday;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng 0 và có 10-11 số")
    private String phone;

    @NotBlank(message = "Vai trò không được để trống")
    private String role; // USER / ADMIN


}
