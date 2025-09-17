package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.service.Interface.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // 📌 Danh sách user
    @GetMapping("/list")
    public String listUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/user/list-user"; // -> list-user.html
    }

//    // 📌 Xem chi tiết
//    @GetMapping("/{id}")
//    public String viewUser(@PathVariable Long id, Model model) {
//        UserDTO user = userService.getUserById(id);
//        model.addAttribute("user", user);
//        return "admin/user/view-user";
//    }

    // 📌 Form tạo mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "/admin/user/create-user";
    }

    // 📌 Lưu user
    @PostMapping
    public String createUser(@ModelAttribute UserDTO userDTO) throws IOException {
        userService.save(userDTO);
        return "redirect:/admin/users/list";
    }

    // 📌 Form sửa
    @GetMapping("/view/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/admin/user/edit-user";
    }
    // 📌 Cập nhật user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") UserDTO userDTO) throws IOException {
        userDTO.setId(id);
        userService.updateUser(id, userDTO);
        return "redirect:/admin/users/list";
    }

    // 📌 Xóa user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users/list";
    }

    @PostMapping("/restore/{id}")
    public String restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return "redirect:/admin/users/recycle";
    }

    @GetMapping("/recycle")
    public String recycleBin(Model model) {
        List<UserDTO> users = userService.getAllDeletedUsers();
        model.addAttribute("users", users);
        return "/admin/user/recycle";
    }

    @DeleteMapping("/permanent-delete/{id}")
    public String permanentDeleteUser(@PathVariable Long id) {
        userService.permanentDeleteUser(id); // xóa thật
        return "redirect:/admin/users/recycle";
    }
}
