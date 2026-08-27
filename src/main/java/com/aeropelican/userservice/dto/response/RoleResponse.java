package com.aeropelican.userservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponse(
        UUID roleId,
        String roleName,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
