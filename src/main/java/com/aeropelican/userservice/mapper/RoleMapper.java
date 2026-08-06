package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.Request.CreateRoleRequest;
import com.aeropelican.userservice.dto.Response.RoleResponse;
import com.aeropelican.userservice.entity.Role;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleMapper {

    public Role toEntity(CreateRoleRequest request) {
        if (request == null) return null;

        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setRoleName(request.roleName());
        role.setDescription(request.description());
        return role;
    }

    public RoleResponse toResponse(Role role) {
        if (role == null) return null;

        return new RoleResponse(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription()
        );
    }
}