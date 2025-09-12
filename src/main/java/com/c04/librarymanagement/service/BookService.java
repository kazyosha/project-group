package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.CreateBookDTO;
import com.c04.librarymanagement.dto.EditBookDTO;
import com.c04.librarymanagement.mapper.BookMapper;
import com.c04.librarymanagement.model.Book;
import com.c04.librarymanagement.model.Category;
import com.c04.librarymanagement.model.Publisher;
import com.c04.librarymanagement.repository.BookRepository;
import com.c04.librarymanagement.repository.CategoryRepository;
import com.c04.librarymanagement.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

    public Book createBook(CreateBookDTO dto) throws IOException {
        // 1. Lưu ảnh vào thư mục uploads
        MultipartFile imageFile = dto.getImage();
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String uploadDir = "uploads/books/";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(uploadDir + fileName);
        imageFile.transferTo(dest);

        String imagePath = uploadDir + fileName;

        // 2. Lấy Category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 3. Lấy Publisher
        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        // 4. Map sang Entity
        Book book = BookMapper.toEntity(dto, imagePath, category, publisher);

        // 5. Lưu DB
        return bookRepository.save(book);
    }
    public Book updateBook(EditBookDTO dto) {
        Book book = bookRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setCondition(dto.getCondition());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        book.setCategory(category);

        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        book.setPublisher(publisher);

        // Nếu có upload ảnh mới
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            String imageUrl = fileStorageService.saveFile(dto.getImageFile()); // gọi qua instance
            book.setImage(imageUrl);
        }

        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
}
