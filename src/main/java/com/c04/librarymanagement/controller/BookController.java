package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.CreateBookDTO;
import com.c04.librarymanagement.dto.EditBookDTO;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.service.BookService;
import com.c04.librarymanagement.repository.CategoryRepository;
import com.c04.librarymanagement.repository.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.c04.librarymanagement.mapper.BookMapper;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/books")
public class BookController {

    private final BookService bookService;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    public BookController(BookService bookService,
                          CategoryRepository categoryRepository,
                          PublisherRepository publisherRepository) {
        this.bookService = bookService;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new CreateBookDTO());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "/admin/books/create";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") CreateBookDTO dto,
                             BindingResult result,
                             Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            return "admin/books/create";
        }
        bookService.createBook(dto);
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        EditBookDTO dto = BookMapper.toEditBookDTO(book);
        model.addAttribute("book", dto);
        return "/admin/books/edit";
    }

    @PostMapping("/update")
    public String updateBook(
            @Valid @ModelAttribute("book") EditBookDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "/admin/books/edit";
        }
        bookService.updateBook(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sách thành công!");
        return "redirect:/admin/books";
    }
}
