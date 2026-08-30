package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateRoleRequest;
import com.aeropelican.userservice.dto.response.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleResponse createRole(CreateRoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(UUID roleId);

    RoleResponse updateRole(UUID roleId, CreateRoleRequest request);

    void deleteRole(UUID roleId);
}
