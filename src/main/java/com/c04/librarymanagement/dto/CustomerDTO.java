package com.c04.librarymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String schoolClass;

    private String address;

    private LocalDate birthDate;
}
