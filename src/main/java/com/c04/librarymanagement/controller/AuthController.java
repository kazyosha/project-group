package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showFormlogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> optionalUser = authService.Login(email, password);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            session.setAttribute("loggedInUser", user);

            // 👉 Chuyển hướng theo role
            return switch (user.getRole().getName()) {
                case ADMIN -> "redirect:/admin/home";
                case LIBRARIAN -> "redirect:/librarian/home";
                default -> "redirect:/auth/login";
            };
        } else {
            model.addAttribute("error", "Sai email hoặc mật khẩu");
            return "auth/login"; // quay lại login.html
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
