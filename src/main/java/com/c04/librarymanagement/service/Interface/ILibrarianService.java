package com.c04.librarymanagement.service.Interface;

import com.c04.librarymanagement.dto.LibrarianDTO;
import com.c04.librarymanagement.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface ILibrarianService {

    LibrarianDTO findById(Long id);

    User findEntityById(Long id);

    void update(LibrarianDTO dto, MultipartFile avatar);

    LibrarianDTO findByEmail(String email);

    User findEntityByEmail(String email);
}
