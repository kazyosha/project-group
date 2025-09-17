package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByDeletedFalse();
    Page<Book> findByDeletedFalse(Pageable pageable);

    Page<Book> findByCategoryIdAndDeletedFalse(Long categoryId, Pageable pageable);
    Page<Book> findByPublisherIdAndDeletedFalse(Long publisherId, Pageable pageable);

    Page<Book> findByCategoryIdAndPublisherIdAndDeletedFalse(Long categoryId, Long publisherId, Pageable pageable);

}
