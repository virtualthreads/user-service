package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.response.UserRoleResponse;
import com.aeropelican.userservice.entity.Role;
import com.aeropelican.userservice.entity.User;
import com.aeropelican.userservice.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRoleMapper {

    private UserRoleMapper() {
    }

    public static UserRole toEntity(User user,
                                    Role role) {

        return UserRole.builder()
                .userRoleId(UUID.randomUUID().toString())
                .user(user)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .build();
    }

    public static UserRoleResponse toResponse(UserRole userRole) {

        return new UserRoleResponse(
                userRole.getUserRoleId(),
                userRole.getUser().getUserId(),
                userRole.getRole().getRoleId(),
                userRole.getRole().getRoleName(),
                userRole.getAssignedAt()
        );
    }

    public static void updateEntity(UserRole userRole,
                                    Role role) {

        userRole.setRole(role);
    }

}