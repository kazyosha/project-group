package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.BorrowRecordDTO;
import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.model.BorrowStatus;
import com.c04.librarymanagement.service.BookService;
import com.c04.librarymanagement.service.BorrowService;
import com.c04.librarymanagement.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class BorrowController {
    private final BorrowService borrowService;
    private final CustomerService customerService;
    private final BookService bookService;

    public BorrowController(BorrowService borrowService,
                            CustomerService customerService,
                            BookService bookService) {
        this.borrowService = borrowService;
        this.customerService = customerService;
        this.bookService = bookService;
    }

    @GetMapping("/borrows")
    public String listBorrowRecords(Model model) {
        model.addAttribute("records", borrowService.getAllBorrowRecords());
        return "/admin/borrow/list";
    }

    @GetMapping("/borrows/create")
    public String showBorrowForm(Model model) {
        model.addAttribute("borrow", new BorrowRecordDTO());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("books", bookService.getAllBooks());
        return "/admin/borrow/create";
    }

    @PostMapping("/borrows/create")
    public String createBorrow(@ModelAttribute("borrow") BorrowRecordDTO dto) {
        borrowService.createBorrowRecord(dto);
        return "redirect:/admin/borrows";
    }

    @PostMapping("/borrows/{id}/status")
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") BorrowStatus status) {
        borrowService.updateStatus(id, status);
        return "redirect:/admin/borrows";
    }

    @GetMapping("/students/search")
    @ResponseBody
    public List<CustomerDTO> searchStudents(@RequestParam("q") String query) {
        return customerService.searchByNameOrCode(query);
    }
}
