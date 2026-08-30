package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignRoleRequest(
        @NotNull(message = "Role id is mandatory") UUID roleId
) {}
