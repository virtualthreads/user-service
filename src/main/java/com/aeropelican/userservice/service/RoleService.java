package com.aeropelican.userservice.service;

import com.aeropelican.userservice.dto.request.CreateRoleRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateRoleRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleResponseDTO createRole(CreateRoleRequestDTO request);

    List<RoleResponseDTO> getAllRoles();

    RoleResponseDTO getRoleById(UUID roleId);

    RoleResponseDTO updateRole(UUID roleId, UpdateRoleRequestDTO request);

    void deleteRole(UUID roleId);
}