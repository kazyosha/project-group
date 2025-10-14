package com.c04.librarymanagement.config;

import com.c04.librarymanagement.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.c04.librarymanagement")
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public resources
                        .requestMatchers("/resources/**", "/auth/login", "/uploads/**", "/js/**", "/css/**", "/images/**").permitAll()

                        // LIBRARIAN có thể xem danh sách trước
                        .requestMatchers("/admin/books").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers("/admin/categories").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers("/admin/borrows").hasAnyRole("ADMIN", "LIBRARIAN")
                        .requestMatchers("/admin/borrows/create").hasAnyRole("ADMIN", "LIBRARIAN")

                        // ADMIN CRUD đầy đủ
                        .requestMatchers("/admin/users/**").hasRole("ADMIN")
                        .requestMatchers("/admin/books/**").hasRole("ADMIN")
                        .requestMatchers("/admin/categories/**").hasRole("ADMIN")
                        .requestMatchers("/admin/borrows/**").hasRole("ADMIN")

                        // Librarian pages
                        .requestMatchers("/librarian/**").hasRole("LIBRARIAN")

                        // Default: authenticated
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(customAuthenticationSuccessHandler()) // ✅ dùng custom success handler
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Custom success handler để điều hướng theo role
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();
            String redirectUrl = "/home"; // mặc định

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                redirectUrl = "/admin";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
                redirectUrl = "/librarian";
            }
            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
