package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponse;
import org.springframework.data.domain.Page;

public interface RoleService {

    RoleResponse createRole(RoleCreateRequestDTO request);

    RoleResponse getRoleById(String id);

    Page<RoleResponse> getAllRoles(int page, int size);

    RoleResponse updateRole(String id,
                            RoleUpdateRequestDTO request);

    void deleteRole(String id);
}