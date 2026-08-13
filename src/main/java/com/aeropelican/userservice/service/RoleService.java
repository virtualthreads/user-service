package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.CreateRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.dto.UpdateRoleRequest;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(UUID roleId);

    RoleResponse updateRole(UUID roleId, UpdateRoleRequest request);

    void deleteRole(UUID roleId);
}
