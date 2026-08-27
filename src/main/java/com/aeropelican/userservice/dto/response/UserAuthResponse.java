package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserAuthResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        Status status,
        String passwordHash
) {
}
