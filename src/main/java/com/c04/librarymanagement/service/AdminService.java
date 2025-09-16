package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.AdminDTO;
import com.c04.librarymanagement.model.RoleType;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.IAdminservice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminservice {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminDTO getAdminUser() {
        User admin = userRepository.findByRole_Name(RoleType.ADMIN)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return AdminDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .birthday(admin.getBirthday())
                .password("") // không hiển thị password
                .build();
    }

    @Override
    public AdminDTO updateAdmin(AdminDTO adminDTO) {
        User admin = userRepository.findById(adminDTO.getId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhone(adminDTO.getPhone());
        admin.setBirthday(adminDTO.getBirthday());

        if(adminDTO.getPassword() != null && !adminDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        }

        userRepository.save(admin);
        return adminDTO;
    }


}
