package com.c04.librarymanagement.service;

import com.c04.librarymanagement.dto.RoleDTO;
import com.c04.librarymanagement.model.Role;
import com.c04.librarymanagement.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> list = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName().name()); // ✅ enum -> String
            list.add(roleDTO);
        }
        return list;
    }
}
