package com.aeropelican.userservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignRoleRequest(
        @NotNull(message = "Role ID is mandatory")
        UUID roleId
) {
}
