package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.CategoryDTO;
import com.c04.librarymanagement.model.Category;
import com.c04.librarymanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CategoryServices {
    private final CategoryRepository categoryRepository;

    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName()))
                .toList();
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        return new CategoryDTO(category.getId(), category.getName());
    }

    public CategoryDTO save(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Tên danh mục đã tồn tại");
        }
        Category category = Category.builder().name(dto.getName()).build();
        return new CategoryDTO(categoryRepository.save(category).getId(), category.getName());
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        category.setName(dto.getName());
        return new CategoryDTO(categoryRepository.save(category).getId(), category.getName());
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new RuntimeException("Không thể xóa danh mục đang chứa sách");
        }
        categoryRepository.delete(category);
    }
}
