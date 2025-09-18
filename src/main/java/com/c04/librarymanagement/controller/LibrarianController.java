package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.LibrarianDTO;
import com.c04.librarymanagement.dto.PasswordForm;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.service.Interface.ILibrarianService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

    private final ILibrarianService librarianService;
    private final PasswordEncoder passwordEncoder;

    public LibrarianController(ILibrarianService librarianService, PasswordEncoder passwordEncoder) {
        this.librarianService = librarianService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String home() {
        return "/home";
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
    public String updateAccount(@Valid @ModelAttribute("librarian") LibrarianDTO dto,
                                BindingResult result,
                                @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                Model model) {

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

        // ⚡ Kiểm tra validate
        if (result.hasErrors()) {
            model.addAttribute("librarian", dto); // giữ lại dữ liệu đã nhập
            return "librarian/edit-librarian"; // trả lại form cũ (HTML của bạn)
        }

        librarianService.update(dto, avatar);

        return "redirect:/home";
    }

    @GetMapping("/account/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("passwordForm", new PasswordForm());
        return "librarian/edit-password";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordForm") PasswordForm form,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "librarian/edit-password";
        }

        // 📌 Lấy user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User principal)) {
            return "redirect:/auth/login";
        }

        String email = principal.getUsername();
        User currentUser = librarianService.findEntityByEmail(email);

        // 📌 Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(form.getOldPassword(), currentUser.getPassword())) {
            model.addAttribute("errorMessage", "Mật khẩu hiện tại không đúng!");
            return "librarian/edit-password";
        }

        // 📌 Kiểm tra xác nhận mật khẩu
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "librarian/edit-password";
        }

        // 📌 Thay đổi mật khẩu
        String encodedPassword = passwordEncoder.encode(form.getNewPassword());
        currentUser.setPassword(encodedPassword);
        librarianService.save(currentUser);

        model.addAttribute("successMessage", "Đổi mật khẩu thành công!");
        return "librarian/edit-password";
    }

}
