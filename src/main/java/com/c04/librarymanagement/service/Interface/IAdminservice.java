package com.c04.librarymanagement.service.Interface;

import com.c04.librarymanagement.dto.AdminDTO;

public interface IAdminservice {
    AdminDTO getAdminUser();

    AdminDTO updateAdmin(AdminDTO adminDTO);


}
