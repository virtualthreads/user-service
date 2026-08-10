package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleMapper {

    public Role toEntity(RoleCreateRequestDTO dto) {

        return Role.builder()
                .roleId(UUID.randomUUID())
                .roleName(dto.getRoleName())
                .description(dto.getDescription())
                .build();
    }

    public RoleResponseDTO toDTO(Role role) {

        return RoleResponseDTO.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}