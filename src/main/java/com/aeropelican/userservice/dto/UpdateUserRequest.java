package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank(message = "First name is mandatory")
        String firstName,

        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format must be valid")
        String email,

        String phoneNumber,

        Gender gender,

        @Past(message = "Date of birth cannot be a future date")
        LocalDate dateOfBirth
) {
}
