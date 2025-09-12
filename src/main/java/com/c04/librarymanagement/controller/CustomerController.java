package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "/admin/customers/list";
    }

    @GetMapping("/customers/create")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new CustomerDTO());
        return "/admin/customers/create";
    }

    @PostMapping("/customers/create")
    public String saveCustomer(@ModelAttribute("customer") CustomerDTO customerDTO) {
        customerService.addCustomer(customerDTO);
        return "redirect:/admin/customers";
    }
}
