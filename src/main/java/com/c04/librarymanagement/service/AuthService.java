package com.c04.librarymanagement.service;

import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IAuthRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final IAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IAuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> Login(String email , String rawPassword) {
        return authRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
}
