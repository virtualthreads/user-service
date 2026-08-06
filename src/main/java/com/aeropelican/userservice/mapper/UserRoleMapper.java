package com.aeropelican.userservice.mapper;
import com.aeropelican.userservice.dto.response.UserRoleResponseDTO;
import com.aeropelican.userservice.entity.UserRole;

public class UserRoleMapper {

    private UserRoleMapper() {
    }

    public static UserRoleResponseDTO toResponseDTO(UserRole entity) {

        if (entity == null) {
            return null;
        }

        return UserRoleResponseDTO.builder()
                .userRoleId(entity.getUserRoleId())
                .userId(entity.getUserId())
                .roleId(entity.getRoleId())
                .assignedAt(entity.getAssignedAt())
                .build();
    }

}
