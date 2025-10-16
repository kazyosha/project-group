package com.c04.librarymanagement.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthday;

}
