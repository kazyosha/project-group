package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.LibrarianDTO;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.ILibrarianService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LibrarianService implements ILibrarianService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String UPLOAD_DIR = "C:/Users/admin/Downloads/uploads/avatars/";

    public LibrarianService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục upload", e);
        }
    }

    @Override
    public LibrarianDTO findById(Long id) {
        return userRepository.findById(id)
                .map(u -> LibrarianDTO.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .imageUrl(u.getImageUrl())
                        .build())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void update(LibrarianDTO dto, MultipartFile avatar) {
        User user = findEntityById(dto.getId());

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + avatar.getOriginalFilename();
                File dest = new File(UPLOAD_DIR + fileName);
                avatar.transferTo(dest);
                user.setImageUrl("avatars/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload file", e);
            }
        }

        userRepository.save(user);
    }
    public LibrarianDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(u -> LibrarianDTO.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .imageUrl(u.getImageUrl())
                        .build())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
