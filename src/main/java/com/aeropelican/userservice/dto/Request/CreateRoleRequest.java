package com.aeropelican.userservice.dto.Request;

public record CreateRoleRequest(
        String roleName,
        String description
) {}