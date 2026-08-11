package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.request.RoleCreateRequestDTO;
import com.aeropelican.userservice.dto.response.RoleResponseDTO;
import com.aeropelican.userservice.entity.Role;

import java.sql.Timestamp;

public class RoleMapper {
        public static Role toEntity(RoleCreateRequestDTO request) {
            if (request == null) {
                return null;
            }

            return Role.builder()
                    .roleName(request.getRoleName())
                    .description(request.getDescription())
                    .build();
        }
        public static RoleResponseDTO toResponseDTO(Role role) {
            if (role == null) {
                return null;
            }

            return RoleResponseDTO.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .description(role.getDescription())
                    .createdAt(Timestamp.valueOf(role.getCreatedAt()))
                    .updatedAt(Timestamp.valueOf(role.getUpdatedAt()))
                    .build();
        }

        // Update existing Entity from Request DTO
        public static void updateEntity(Role role, RoleCreateRequestDTO request) {
            if (role == null || request == null) {
                return;
            }

            role.setRoleName(request.getRoleName());
            role.setDescription(request.getDescription());
        }

}
