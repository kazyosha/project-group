package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.BorrowRecordDTO;
import com.c04.librarymanagement.dto.CustomerDTO;
import com.c04.librarymanagement.model.BorrowStatus;
import com.c04.librarymanagement.service.BookService;
import com.c04.librarymanagement.service.BorrowService;
import com.c04.librarymanagement.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "admin/borrow/list";
    }

    @GetMapping("/borrows/create")
    public String showBorrowForm(Model model) {
        model.addAttribute("borrow", new BorrowRecordDTO());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("books", bookService.getAllBooks());
        return "admin/borrow/create";
    }

    @PostMapping("/borrows/create")
    public String createBorrow(@Valid @ModelAttribute("borrow") BorrowRecordDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("books", bookService.getAllBooks());
            return "admin/borrow/create";
        }
        borrowService.createBorrowRecord(dto);
        return "redirect:/admin/borrows";
    }

    @PostMapping("/borrows/{id}/status")
    @ResponseBody
    public String updateStatus(@PathVariable("id") Long id,
                               @RequestParam("status") BorrowStatus status) {
        borrowService.updateStatus(id, status);
        return "success";
    }

    @GetMapping("/borrows/customers/search")
    @ResponseBody
    public List<CustomerDTO> searchStudentsForBorrow(@RequestParam("q") String query) {
        return customerService.searchActiveCustomersForBorrow(query);
    }

    @GetMapping("/borrows/search-list")
    @ResponseBody
    public Page<BorrowRecordDTO> searchBorrowRecordsApi(
            @RequestParam(required = false) BorrowStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return borrowService.searchBorrowRecords(status, keyword, pageable);
    }


}
