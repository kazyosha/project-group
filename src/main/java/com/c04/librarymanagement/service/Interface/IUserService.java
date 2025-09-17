package com.c04.librarymanagement.service.Interface;

import com.c04.librarymanagement.dto.UserDTO;
import com.c04.librarymanagement.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO) throws IOException;
    UserDTO updateUser(Long id, UserDTO userDTO) throws IOException;
    void deleteUser(Long id);

    void restoreUser(Long id);

    List<UserDTO> getAllDeletedUsers();

    void permanentDeleteUser(Long id);

    UserDTO save(UserDTO userDTO) throws IOException;

}



