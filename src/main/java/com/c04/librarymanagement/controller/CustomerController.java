package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String listCustomers(Model model,
                                @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 1); // 10 record/trang
        Page<CustomerDTO> customersPage = customerService.getCustomersPage(pageable);

        model.addAttribute("customers", customersPage.getContent());

        model.addAttribute("customersPage", customersPage);
        model.addAttribute("currentPage", page);
        return "/admin/customers/list";
    }

    @GetMapping("/customers/deleted")
    public String deletedCustomers(Model model,
                                   @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10); // 10 record/trang
        Page<CustomerDTO> deletedPage = customerService.getDeletedCustomersPage(pageable);

        model.addAttribute("customers", deletedPage.getContent());

        model.addAttribute("customersPage", deletedPage);
        model.addAttribute("currentPage", page);
        return "/admin/customers/deleted";
    }

    @GetMapping("/customers/create")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        return "/admin/customers/create";
    }

    @PostMapping("/customers/create")
    public String saveCustomer(@Valid @ModelAttribute("customer") CustomerDTO customerDTO,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/customers/create";
        }
        customerService.addCustomer(customerDTO);
        return "redirect:/admin/customers";
    }

    // ==== Xóa mềm ====
    @GetMapping("/customers/{id}/delete")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.softDeleteCustomer(id);
        return "redirect:/admin/customers";
    }

    // ==== Khôi phục ====
    @PostMapping("/customers/{id}/restore")
    public String restoreCustomer(@PathVariable Long id) {
        customerService.restoreCustomer(id);
        return "redirect:/admin/customers/deleted";
    }

    // ==== Xóa cứng ====
    @PostMapping("/customers/{id}/hard-delete")
    public String hardDeleteCustomer(@PathVariable Long id) {
        customerService.hardDelete(id);
        return "redirect:/admin/customers/deleted";
    }

    // ==== Xóa toàn bộ thùng rác ====
    @PostMapping("/customers/deleted/clear")
    public String clearDeletedCustomers() {
        customerService.clearDeleted();
        return "redirect:/admin/customers/deleted";
    }

    // ==== Cập nhật khách hàng ====
    @GetMapping("/customers/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        CustomerDTO customer = customerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        model.addAttribute("customer", customer);
        return "/admin/customers/edit";
    }

    @PostMapping("/customers/update")
    public String updateCustomer(@Valid @ModelAttribute("customer") CustomerDTO dto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/customers/edit";
        }
        customerService.updateCustomer(dto);
        return "redirect:/admin/customers";
    }
}
