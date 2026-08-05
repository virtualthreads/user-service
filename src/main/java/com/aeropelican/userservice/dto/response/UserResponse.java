package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;

import java.time.LocalDate;

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
        UserStatus status

) {
}