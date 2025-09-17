package com.c04.librarymanagement.repository;

import com.c04.librarymanagement.model.Role;
import com.c04.librarymanagement.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
