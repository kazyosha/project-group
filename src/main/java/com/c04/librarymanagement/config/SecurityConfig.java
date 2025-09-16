//package com.c04.librarymanagement.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan("com.c04.librarymanagement")
//public class SecurityConfig {
//    private final CustomUserDetailsService customUserDetailsService;
//
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(
//                                "/resources/**",
//                                "/auth/login",
//                                "/uploads/**",
//                                "/js/**",
//                                "/css/**",
//                                "/images/**"
//                        ).permitAll()
//                        .requestMatchers("/**").hasRole("ADMIN")
//                        .requestMatchers("/home/**", "/admin/borrows/**", "/admin/customers/**").hasRole("LIBRARIAN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/auth/login")
//                        .loginProcessingUrl("/auth/login")
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                        .successHandler(customAuthenticationSuccessHandler()) // ✅ dùng custom success handler
//                        .failureUrl("/auth/login?error")
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/auth/login?logout")
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(customUserDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // ✅ Custom success handler để điều hướng theo role
//    @Bean
//    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//            var authorities = authentication.getAuthorities();
//            String redirectUrl = "/home"; // mặc định
//
//            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//                redirectUrl = "/admin";
//            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"))) {
//                redirectUrl = "/librarian";
//            }
//            response.sendRedirect(redirectUrl);
//        };
//    }
//}
