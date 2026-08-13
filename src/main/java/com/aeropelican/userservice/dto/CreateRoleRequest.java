package com.aeropelican.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(
        @NotBlank(message = "Role name is mandatory")
        String roleName,

        String description
) {
}
