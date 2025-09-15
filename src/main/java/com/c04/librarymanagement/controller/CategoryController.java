package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.CategoryDTO;
import com.c04.librarymanagement.service.CategoryServices;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    private CategoryServices categoryServices;

    public CategoryController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @GetMapping("/categories")
    public String list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<CategoryDTO> categoryPage = categoryServices.findAll(PageRequest.of(page, size));
        model.addAttribute("categoryPage", categoryPage);
        return "admin/category/list";
    }

    @GetMapping("/categories/create")
    public String createForm(Model model) {
        model.addAttribute("category", new CategoryDTO());
        return "admin/category/create";
    }

    @PostMapping("/categories/create")
    public String create(@Valid @ModelAttribute("category") CategoryDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/category/create";
        }
        categoryServices.save(dto);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryServices.findById(id));
        return "admin/category/edit";
    }

    @PostMapping("/categories/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("category") CategoryDTO dto,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/edit";
        }
        categoryServices.update(id, dto);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryServices.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa danh mục vì đang có sách sử dụng!");
        }
        return "redirect:/admin/categories";
    }

}
