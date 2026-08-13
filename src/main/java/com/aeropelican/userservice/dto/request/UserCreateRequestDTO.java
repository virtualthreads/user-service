package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.Gender;

import java.time.LocalDate;

public record UserCreateRequestDTO(

        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String passwordHash,
        Gender gender,
        LocalDate dateOfBirth

) {
}