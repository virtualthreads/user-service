package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank(message = "First name is mandatory") String firstName,
        String lastName,
        @NotBlank(message = "Email is mandatory") @Email(message = "Email format is invalid") String email,
        String phoneNumber,
        @NotNull(message = "Gender is mandatory") Gender gender,
        @NotNull(message = "Date of birth is mandatory") @Past(message = "Date of birth cannot be a future date") LocalDate dateOfBirth
) {}
