package com.c04.librarymanagement.service;

import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IAuthRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IAuthRepository authRepository;

    public CustomUserDetailsService(IAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm user theo email
        User user = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Lấy role của user
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getName());

        // Trả về UserDetails của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),       // username
                user.getPassword(),    // password (BCrypt)
                Collections.singleton(authority) // quyền
        );
    }
}
