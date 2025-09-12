package com.c04.librarymanagement.util;

import com.c04.librarymanagement.service.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class TestLoginConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TestLoginConfig(CustomUserDetailsService customUserDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner testLogin() {
        return args -> {
            String inputEmail = "admin@gmail.com";   // giả sử nhập từ form
            String inputPassword = "admin";          // giả sử nhập từ form

            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(inputEmail);

                System.out.println("===== TEST LOGIN =====");
                System.out.println("Email nhập vào: " + inputEmail);
                System.out.println("Password nhập vào: " + inputPassword);
                System.out.println("Password trong DB (BCrypt): " + userDetails.getPassword());
                System.out.println("Authorities: " + userDetails.getAuthorities());

                boolean match = passwordEncoder.matches(inputPassword, userDetails.getPassword());
                System.out.println("Kết quả so sánh mật khẩu: " + match);
                System.out.println("======================");

            } catch (Exception e) {
                System.err.println("Login thất bại: " + e.getMessage());
            }
        };
    }
}
