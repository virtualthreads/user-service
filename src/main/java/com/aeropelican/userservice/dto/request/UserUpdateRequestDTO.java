package com.aeropelican.userservice.dto.request;
import com.aeropelican.userservice.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class UserUpdateRequestDTO {
    @NotBlank
    private String firstName;
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @Pattern(regexp = "[0-9]",
            message = "Phone number contains 10 digits")
    private String phoneNumber;
    private Gender gender;
    @PastOrPresent
    private LocalDate dateOfBirth;
    private String password;
}
