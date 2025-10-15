package com.c04.librarymanagement.service.UploadService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class AvatarStorageService {

    private final Path rootLocation = Paths.get("uploads/avatars");

    public AvatarStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize avatar storage!", e);
        }
    }

    public String saveAvatar(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("File trống!");
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(filename).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/avatars/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu avatar.", e);
        }
    }
}

