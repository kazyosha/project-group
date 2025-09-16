package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.BookDTO;
import com.c04.librarymanagement.dto.CreateBookDTO;
import com.c04.librarymanagement.dto.EditBookDTO;
import com.c04.librarymanagement.dto.response.ListBookResponse;
import com.c04.librarymanagement.dto.BorrowBookDTO;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.model.Category;
import com.c04.librarymanagement.model.Publisher;
import com.c04.librarymanagement.repository.BookRepository;
import com.c04.librarymanagement.repository.CategoryRepository;
import com.c04.librarymanagement.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final FileStorageService fileStorageService;

    public BookService(BookRepository bookRepository,
                       CategoryRepository categoryRepository,
                       PublisherRepository publisherRepository,
                       FileStorageService fileStorageService) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.fileStorageService = fileStorageService;
    }

    // ================= Mapping =================
    private BookDTO toBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setCondition(book.getCondition());
        dto.setImage(book.getImage());
        dto.setCategoryName(book.getCategory() != null ? book.getCategory().getName() : null);
        dto.setPublisherName(book.getPublisher() != null ? book.getPublisher().getName() : null);
        return dto;
    }

    private Book toEntity(CreateBookDTO dto) {
        Book book = new Book();

        // Lưu file và lấy path
        String imagePath = null;
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            imagePath = fileStorageService.saveFile(dto.getImageFile());
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setCondition(dto.getCondition());
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setImage(imagePath);
        book.setDeleted(false);

        return book;
    }

    public EditBookDTO toEditBookDTO(Book book) {
        EditBookDTO dto = new EditBookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setCondition(book.getCondition());
        dto.setCategoryId(book.getCategory() != null ? book.getCategory().getId() : null);
        dto.setPublisherId(book.getPublisher() != null ? book.getPublisher().getId() : null);
        dto.setCurrentImage(book.getImage());
        return dto;
    }

    public ListBookResponse getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByDeletedFalse(pageable);

        List<BookDTO> dtos = bookPage.getContent()
                .stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());

        return new ListBookResponse(dtos, bookPage.getNumber() + 1, bookPage.getTotalPages());
    }

    public ListBookResponse getBooksByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable);

        List<BookDTO> dtos = bookPage.getContent()
                .stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());

        return new ListBookResponse(dtos, bookPage.getNumber() + 1, bookPage.getTotalPages());
    }

    public Book createBook(CreateBookDTO dto) {
        Book book = toEntity(dto); // xử lý MultipartFile + mapping Category, Publisher
        return bookRepository.save(book);
    }

    public Book updateBook(EditBookDTO dto) {
        Book book = bookRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setCondition(dto.getCondition());

        book.setCategory(categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        book.setPublisher(publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found")));

        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            book.setImage(fileStorageService.saveFile(dto.getImageFile()));
        }

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setDeleted(true);
        bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    private BorrowBookDTO toDTO(Book book) {
        return BorrowBookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .condition(book.getCondition() != null ? book.getCondition().name() : null)
                .build();
    }

    public EditBookDTO getEditBookDTO(Long id) {
        Book book = getBookById(id);
        return toEditBookDTO(book);
    }
    public ListBookResponse getBooksByPublisher(Long publisherId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByPublisherIdAndDeletedFalse(publisherId, pageable);

        List<BookDTO> dtos = bookPage.getContent()
                .stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());

        return new ListBookResponse(dtos, bookPage.getNumber() + 1, bookPage.getTotalPages());
    }


    public List<BorrowBookDTO> getAllBooks() {
        return bookRepository.findByDeletedFalse()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ListBookResponse getBooksByCategoryAndPublisher(Long categoryId, Long publisherId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepository.findByCategoryIdAndPublisherIdAndDeletedFalse(categoryId, publisherId, pageable);

        List<BookDTO> dtos = bookPage.getContent()
                .stream()
                .map(this::toBookDTO)
                .collect(Collectors.toList());

        return new ListBookResponse(dtos, bookPage.getNumber() + 1, bookPage.getTotalPages());
    }

}
