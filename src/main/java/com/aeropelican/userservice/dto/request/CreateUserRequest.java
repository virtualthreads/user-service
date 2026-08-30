package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "First name is mandatory")
        String firstName,

        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format is invalid")
        String email,

        String phoneNumber,

        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,

        @NotNull(message = "Gender is mandatory")
        Gender gender,

        @NotNull(message = "Date of birth is mandatory")
        @Past(message = "Date of birth cannot be a future date")
        LocalDate dateOfBirth
) {}
