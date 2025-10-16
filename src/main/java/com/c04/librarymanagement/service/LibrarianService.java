package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.LibrarianDTO;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.ILibrarianService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LibrarianService implements ILibrarianService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 📌 Đường dẫn tương đối trong dự án
    private static final String UPLOAD_DIR = "uploads/avatars/";

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

//        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
//            user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + avatar.getOriginalFilename();
                Path destination = Paths.get(UPLOAD_DIR, fileName);
                Files.copy(avatar.getInputStream(), destination);

                // 🔹 Lưu đường dẫn web, không lưu full path
                user.setImageUrl("/uploads/avatars/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload file", e);
            }
        }

        userRepository.save(user);
    }

    @Override
    public void save(User currentUser) {
        userRepository.save(currentUser);
    }
    @Override
    public boolean checkOldPassword(Long userId, String oldPassword) {
        User user = findEntityById(userId);
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    // 📌 Update mật khẩu mới
    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = findEntityById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
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
