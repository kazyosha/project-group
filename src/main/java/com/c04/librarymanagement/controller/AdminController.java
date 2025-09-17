package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.AdminDTO;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.IAdminservice;
import com.c04.librarymanagement.service.Interface.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final IAdminservice adminService;
    private final IUserService userService;
    private final IUserRepository userRepository;

    public AdminController(IAdminservice adminService, IUserService userService, IUserRepository userRepository) {
        this.adminService = adminService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String home() {
        return "admin/home-admin";
    }

    @GetMapping("list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/list-user";
    }
    @GetMapping("/edit")
    public String showEditAdminForm(Model model) {
        AdminDTO admin = adminService.getAdminUser(); // hoặc lấy admin hiện tại
        model.addAttribute("admin", admin); // 🔹 phải trùng với ${admin}
        return "admin/edit-admin"; // tên file html
    }
    @PostMapping("/profile/update")
    public String updateAdmin(@ModelAttribute("admin") AdminDTO adminDTO) {
        adminService.updateAdmin(adminDTO); // service xử lý update admin
        return "redirect:/admin"; // quay về dashboard
    }

}
