package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.UserStatus;

import java.time.LocalDate;

public record UserUpdateRequestDTO(

        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth,
        UserStatus status

) {
}