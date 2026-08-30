package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank(message = "Role name is mandatory") String roleName,
        String description
) {}
