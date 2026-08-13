package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.AssignRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {

    RoleResponse assignRole(UUID userId, AssignRoleRequest request);

    List<RoleResponse> getUserRoles(UUID userId);

    void removeRole(UUID userId, UUID roleId);
}
