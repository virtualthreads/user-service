package com.aeropelican.userservice.dto.Response;

import com.aeropelican.userservice.entity.enums.Gender;
import com.aeropelican.userservice.entity.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        String userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth,
        Boolean emailVerified,
        Boolean phoneVerified,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}