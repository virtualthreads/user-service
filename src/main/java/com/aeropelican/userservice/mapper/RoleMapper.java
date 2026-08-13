package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.request.RoleUpdateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponse;
import com.aeropelican.userservice.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class RoleMapper {

    private RoleMapper() {
    }

    public static Role toEntity(RoleCreateRequestDTO request) {

        return Role.builder()
                .roleId(UUID.randomUUID().toString())
                .roleName(request.roleName())
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static RoleResponse toResponse(Role role) {

        return new RoleResponse(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription()
        );
    }

    public static void updateEntity(Role role,
                                    RoleUpdateRequestDTO request) {

        if (request.roleName() != null) {
            role.setRoleName(request.roleName());
        }

        if (request.description() != null) {
            role.setDescription(request.description());
        }

        role.setUpdatedAt(LocalDateTime.now());
    }

}