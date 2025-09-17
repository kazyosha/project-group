package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.model.RoleType;
import com.c04.librarymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);

    List<User> findAllByDeletedTrue();
    List<User> findAllByDeletedFalse();
    Optional<User> findByRole_Name(RoleType roleName);

}
