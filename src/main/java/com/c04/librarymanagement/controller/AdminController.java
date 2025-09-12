package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IUserService userService;

    public AdminController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home() {
        return "/admin/home-admin";
    }

    @GetMapping("/create/user")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "/admin/create-user";
    }

    @PostMapping("/create/user")
    public String createUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

}
