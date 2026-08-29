package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserCreateRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;
    @NotBlank
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email cannot exceed 200 characters")
    private String email;
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
    private String password;
    private Gender gender;
    @PastOrPresent
    private LocalDate dateOfBirth;
}
