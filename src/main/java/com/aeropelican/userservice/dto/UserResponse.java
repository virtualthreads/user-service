package com.aeropelican.userservice.dto;

import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth,
        Boolean emailVerified,
        Boolean phoneVerified,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
