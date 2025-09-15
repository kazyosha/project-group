package com.c04.librarymanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Giữ Builder nếu muốn tạo object dễ dàng
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private LocalDate birthDate;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String avatar;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(updatable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now();

    private java.time.LocalDateTime updatedAt = java.time.LocalDateTime.now();
}
