package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.entity.Gender;
import com.aeropelican.userservice.entity.UserStatus;
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