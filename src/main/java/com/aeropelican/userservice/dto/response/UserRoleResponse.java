package com.aeropelican.userservice.dto.response;

import java.time.LocalDateTime;

public record UserRoleResponse(

        String userRoleId,
        String userId,
        String roleId,
        String roleName,
        LocalDateTime assignedAt

) {
}