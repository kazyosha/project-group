package com.c04.librarymanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageService {

    // Đường dẫn cố định (tùy chỉnh lại theo yêu cầu)
    private static final String UPLOAD_DIR = "C:/Users/admin/Downloads/uploads";

    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Tạo thư mục nếu chưa có
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file mới để tránh trùng
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Lưu file
        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);

        return fileName; // Trả về tên file (lưu trong DB)
    }
}
