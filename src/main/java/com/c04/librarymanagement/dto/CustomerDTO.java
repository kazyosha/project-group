package com.c04.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank(message = "Mã khách hàng không được để trống")
    @Size(max = 20, message = "Mã khách hàng tối đa 20 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Mã KH chỉ được chứa chữ, số, dấu gạch ngang hoặc gạch dưới")
    private String code;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên tối đa 100 ký tự")
    private String name;

    @Size(max = 100, message = "Lớp/Trường tối đa 100 ký tự")
    private String schoolClass;

    @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự")
    private String address;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate birthDate;
}
