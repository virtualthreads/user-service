package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Gender;
import java.time.LocalDate;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password,
        Gender gender,
        LocalDate dateOfBirth
) {}