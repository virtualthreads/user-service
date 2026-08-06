package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(
            max = 100,
            message = "First name must not exceed 100 characters"
    )
    private String firstName;

    @Size(
            max = 100,
            message = "Last name must not exceed 100 characters"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(
            max = 255,
            message = "Email must not exceed 255 characters"
    )
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(
            min = 8,
            max = 100,
            message = "Password must be between 8 and 100 characters"
    )
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character"
    )
    private String password;

    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}