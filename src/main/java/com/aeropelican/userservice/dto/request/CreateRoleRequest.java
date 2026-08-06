package com.aeropelican.userservice.dto.request;

public record CreateRoleRequest(
        String roleName,
        String description
) {}