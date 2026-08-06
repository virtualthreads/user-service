package com.aeropelican.userservice.dto.Request;

import com.aeropelican.userservice.entity.enums.Gender;
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