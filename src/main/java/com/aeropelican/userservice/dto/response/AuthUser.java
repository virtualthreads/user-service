package com.aeropelican.userservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthUser(
        UUID userId,
        String firstname,
        String lastname,
        String email,
        String hashedPassword,
        String role
) {
}
