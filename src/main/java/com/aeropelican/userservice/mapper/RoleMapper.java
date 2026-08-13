package com.aeropelican.userservice.mapper;

import com.aeropelican.userservice.dto.CreateRoleRequest;
import com.aeropelican.userservice.dto.RoleResponse;
import com.aeropelican.userservice.dto.UpdateRoleRequest;
import com.aeropelican.userservice.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoleMapper {

    private static final Logger logger = LoggerFactory.getLogger(RoleMapper.class);

    public Role toEntity(CreateRoleRequest request) {
        logger.debug("Mapping CreateRoleRequest to Role entity for roleName: {}", request.roleName());
        Role role = new Role();
        role.setRoleName(request.roleName());
        role.setDescription(request.description());
        return role;
    }

    public void updateEntityFromDto(UpdateRoleRequest request, Role role) {
        logger.debug("Updating Role entity (roleId: {}) from UpdateRoleRequest", role.getRoleId());
        role.setRoleName(request.roleName());
        role.setDescription(request.description());
        role.setUpdatedAt(LocalDateTime.now());
    }

    public RoleResponse toResponse(Role role) {
        if (role == null) {
            logger.debug("Attempted to map null Role entity to RoleResponse");
            return null;
        }
        logger.debug("Mapping Role entity to RoleResponse for roleId: {}", role.getRoleId());
        return new RoleResponse(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}
