package com.aeropelican.userservice.dto.Response;

public record RoleResponse(
        String roleId,
        String roleName,
        String description
) {}