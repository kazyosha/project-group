package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.LibrarianDTO;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.service.Interface.ILibrarianService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final ILibrarianService librarianService;

    public LibrarianController(ILibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @GetMapping
    public String home() {
        return "librarian/home-librarian";
    }

    @GetMapping("/account/edit")
    public String editAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            String email = ((org.springframework.security.core.userdetails.User) principal).getUsername();

            LibrarianDTO dto = librarianService.findByEmail(email); // cần service tìm theo email
            model.addAttribute("librarian", dto);
            return "librarian/edit-librarian";
        }

        return "redirect:/auth/login";
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute("librarian") LibrarianDTO dto,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof org.springframework.security.core.userdetails.User)) {
            return "redirect:/auth/login";
        }

        String email = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        User currentUser = librarianService.findEntityByEmail(email); // cần method tìm User theo email

        dto.setId(currentUser.getId());
        librarianService.update(dto, avatar);

        // không cần set lại session, SecurityContext tự quản lý authentication
        return "redirect:/librarian/home";
    }
}
