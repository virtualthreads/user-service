package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Gender;

import java.time.LocalDate;

public record UpdateUserRequest(

        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth

) {
}