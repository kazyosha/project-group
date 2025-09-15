package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface IUserService {
    User createUser(UserDTO userDTO);

    // Lấy tất cả user
    List<UserDTO> getAllUsers();

    // Tìm user theo ID
    Optional<UserDTO> getUserById(Long id);

    // Xóa user theo ID
    void deleteUser(Long id);
}
