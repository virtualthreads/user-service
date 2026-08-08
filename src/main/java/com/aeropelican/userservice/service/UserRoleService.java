package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.AssignRoleRequestDTO;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserRoleService {

    /**
     * Assign a role to a user.
     */
    UserRoleResponseDTO assignRole(
            UUID userId,
            AssignRoleRequestDTO request
    );

    /**
     * Get all roles assigned to a user.
     */
    List<UserRoleResponseDTO> getUserRoles(UUID userId);

    /**
     * Remove a role from a user.
     */
    void removeRole(UUID userId, UUID roleId);
}