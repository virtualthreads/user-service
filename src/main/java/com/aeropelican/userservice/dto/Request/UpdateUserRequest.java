package com.aeropelican.userservice.dto.Request;

import com.aeropelican.userservice.entity.enums.Gender;
import java.time.LocalDate;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Gender gender,
        LocalDate dateOfBirth
) {}