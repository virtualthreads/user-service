package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponseDTO {
    UUID userId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Gender gender;
    LocalDate dateOfBirth;
    Boolean emailVerified;
    Boolean phoneVerified;
    UserStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}