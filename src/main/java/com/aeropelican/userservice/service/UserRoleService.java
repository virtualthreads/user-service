package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.UserRoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.UserRoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponse;
import org.springframework.data.domain.Page;

public interface UserRoleService {

    UserRoleResponse assignRole(UserRoleCreateRequestDTO request);

    UserRoleResponse getUserRoleById(String id);

    Page<UserRoleResponse> getAllUserRoles(int page, int size);

    UserRoleResponse updateUserRole(
            String id,
            UserRoleUpdateRequestDTO request);

    void deleteUserRole(String id);

}