package com.aeropelican.userservice.dto.request;
import com.aeropelican.userservice.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class UserUpdateRequestDTO {
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        @Pattern(
                regexp = "^[A-Za-z ]+$",
                message = "First name can contain only letters and spaces"
        )
        private String firstName;

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        @Pattern(
                regexp = "^[A-Za-z ]+$",
                message = "Last name can contain only letters and spaces"
        )
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        private String phoneNumber;

        @NotNull(message = "Gender is required")
        private Gender gender;

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be a past date")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
        )
        private String password;

}
