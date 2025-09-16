package com.c04.librarymanagement.controller;

import com.c04.librarymanagement.dto.BookDTO;
import com.c04.librarymanagement.dto.CreateBookDTO;
import com.c04.librarymanagement.dto.EditBookDTO;
import com.c04.librarymanagement.dto.response.ListBookResponse;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.service.BookService;
import com.c04.librarymanagement.repository.CategoryRepository;
import com.c04.librarymanagement.repository.PublisherRepository;
import com.c04.librarymanagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/books")
public class BookController {

    private final BookService bookService;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryService categoryService;

    public BookController(BookService bookService,
                          CategoryRepository categoryRepository,
                          PublisherRepository publisherRepository,
                          CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listBooks(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size,
                            @RequestParam(value = "categoryId", required = false) String categoryId,
                            @RequestParam(value = "publisherId", required = false) String publisherId,
                            Model model) {

        if (page < 1) {
            page = 1;
        } else {
            page -= 1;
        }

        ListBookResponse listBookResponse;

        // Nếu chọn cả category và publisher
        if (categoryId != null && !categoryId.isEmpty()
                && publisherId != null && !publisherId.isEmpty()) {
            Long catId = Long.parseLong(categoryId);
            Long pubId = Long.parseLong(publisherId);
            listBookResponse = bookService.getBooksByCategoryAndPublisher(catId, pubId, page, size);
            model.addAttribute("selectedCategoryId", catId);
            model.addAttribute("selectedPublisherId", pubId);
        }
        // Nếu chỉ chọn category
        else if (categoryId != null && !categoryId.isEmpty()) {
            Long catId = Long.parseLong(categoryId);
            listBookResponse = bookService.getBooksByCategory(catId, page, size);
            model.addAttribute("selectedCategoryId", catId);
            model.addAttribute("selectedPublisherId", null);
        }
        // Nếu chỉ chọn publisher
        else if (publisherId != null && !publisherId.isEmpty()) {
            Long pubId = Long.parseLong(publisherId);
            listBookResponse = bookService.getBooksByPublisher(pubId, page, size);
            model.addAttribute("selectedPublisherId", pubId);
            model.addAttribute("selectedCategoryId", null);
        }
        // Không chọn gì
        else {
            listBookResponse = bookService.getAllBooks(page, size);
            model.addAttribute("selectedCategoryId", null);
            model.addAttribute("selectedPublisherId", null);
        }

        model.addAttribute("books", listBookResponse.getBooks());
        model.addAttribute("totalPages", listBookResponse.getTotalPage());
        model.addAttribute("currentPage", listBookResponse.getCurrentPage());

        // load dropdown
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("publishers", publisherRepository.findAll());

        return "admin/books/list";
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
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            return "admin/books/create";
        }
        bookService.createBook(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sách mới thành công!");
        return "redirect:/admin/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        EditBookDTO dto = bookService.toEditBookDTO(book);
        model.addAttribute("book", dto);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "/admin/books/edit";
    }

    @PostMapping("/update")
    public String updateBook(@Valid @ModelAttribute("book") EditBookDTO dto,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("publishers", publisherRepository.findAll());
            return "/admin/books/edit";
        }
        bookService.updateBook(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sách thành công!");
        return "redirect:/admin/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sách thành công!");
        return "redirect:/admin/books";
    }
}
