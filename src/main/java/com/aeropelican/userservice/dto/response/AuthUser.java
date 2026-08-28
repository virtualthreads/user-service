package com.aeropelican.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record AuthUser(
        String firstname,
        String lastname,
        String email,
        String hashedPassword
) {
}
