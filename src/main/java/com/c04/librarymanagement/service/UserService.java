package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.model.Role;
import com.c04.librarymanagement.model.RoleType;
import com.c04.librarymanagement.model.User;
import com.c04.librarymanagement.repository.IRoleRepository;
import com.c04.librarymanagement.repository.IUserRepository;
import com.c04.librarymanagement.service.Interface.IUserService;
import com.c04.librarymanagement.service.UploadService.AvatarStorageService;
import com.c04.librarymanagement.util.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AvatarStorageService avatarStorageService;

    public UserService(IUserRepository userRepository,
                       IRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, AvatarStorageService avatarStorageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.avatarStorageService = avatarStorageService;
    }

    // ================== MAPPING ==================
    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole().getName().name())
                .build();
    }

    private User toEntity(UserDTO dto) {
        Role role = roleRepository.findByName(RoleType.valueOf(dto.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found: " + dto.getRole()));

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imageUrl(dto.getImageUrl())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(role)
                .deleted(false)
                .build();
    }

    // ================== SERVICE METHODS ==================
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByDeletedFalse()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws IOException {
        User user = toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userDTO.getImageUrlFile() != null && !userDTO.getImageUrlFile().isEmpty()) {
            String url = avatarStorageService.saveAvatar(userDTO.getImageUrlFile());
            user.setImageUrl(url);
        }

        return toDTO(userRepository.save(user));
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
            String url = avatarStorageService.saveAvatar(userDTO.getImageUrlFile());
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
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void restoreUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setDeleted(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllDeletedUsers() {
        return userRepository.findAllByDeletedTrue()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void permanentDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public UserDTO save(UserDTO userDTO) throws IOException {
        if (userDTO.getId() == null) {
            return createUser(userDTO);
        } else {
            return updateUser(userDTO.getId(), userDTO);
        }
    }
}
