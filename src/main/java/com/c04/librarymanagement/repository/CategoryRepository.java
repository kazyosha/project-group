package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.Category;
import com.c04.librarymanagement.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Publisher> findByName(String name);
}
