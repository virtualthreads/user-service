package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.CreateRoleRequestDTO;
import com.aeropelican.userservice.dto.request.UpdateRoleRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(CreateRoleRequestDTO request) {

        if (request == null) {
            return null;
        }

        return Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .build();
    }

    public RoleResponseDTO toResponse(Role role) {

        if (role == null) {
            return null;
        }

        return RoleResponseDTO.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }

    public void updateEntity(
            Role role,
            UpdateRoleRequestDTO request
    ) {

        if (role == null || request == null) {
            return;
        }

        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());
    }
}