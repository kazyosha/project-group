package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.model.Role;
import com.c04.librarymanagement.model.RoleType;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IRoleRepository;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.IUserService;
import com.c04.librarymanagement.util.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().getName().name()) // lấy ADMIN/USER
                .build();
    }

    private User toEntity(UserDTO dto) {
        Role role = roleRepository.findByName(RoleType.valueOf(dto.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found: " + dto.getRole()));
        System.out.println("Role: " + role.getName());

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imageUrl(dto.getImageUrl())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(role)
                .build();
    }
    private String saveAvatar(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        // 📌 Lưu cố định vào thư mục Downloads/uploads
        Path uploadPath = Paths.get("C:/Users/admin/Downloads/uploads/avatars");
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        file.transferTo(filePath.toFile());

        // 👉 Trả về đường dẫn ảo để hiển thị (sẽ map bằng ResourceHandler)
        return "/uploads/avatars/" + fileName;
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByDeletedFalse().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws IOException {
        User user = toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userDTO.getImageUrlFile() != null && !userDTO.getImageUrlFile().isEmpty()) {
            String url = saveAvatar(userDTO.getImageUrlFile());
            user.setImageUrl(url);
        }

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws IOException {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setBirthday(userDTO.getBirthday());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        if (userDTO.getImageUrlFile() != null && !userDTO.getImageUrlFile().isEmpty()) {
            String url = saveAvatar(userDTO.getImageUrlFile());
            existingUser.setImageUrl(url);
        }

        Role role = roleRepository.findByName(RoleType.valueOf(userDTO.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDTO.getRole()));
        existingUser.setRole(role);

        return toDTO(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeleted(true);  // chỉ set deleted = true
        userRepository.save(user); // lưu lại thay đổi
    }
    @Override
    public void restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        user.setDeleted(false); // 🔹 khôi phục
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllDeletedUsers() {
        return userRepository.findAllByDeletedTrue()
                .stream()
                .map(this::toDTO)  // dùng toDTO sẵn có
                .toList();
    }

    @Override
    public void permanentDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        userRepository.delete(user); // xóa thật
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws IOException {
        if (userDTO.getId() == null) {
            // Nếu chưa có id thì tạo mới
            return createUser(userDTO);
        } else {
            // Nếu có id thì update
            return updateUser(userDTO.getId(), userDTO);
        }
    }


}
