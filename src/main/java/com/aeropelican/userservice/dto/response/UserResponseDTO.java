package com.aeropelican.userservice.dto.response;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    // ==========================
    // User Information
    // ==========================

    private UUID userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    // ==========================
    // Personal Information
    // ==========================

    private Gender gender;

    private LocalDate dateOfBirth;

    // ==========================
    // Verification Details
    // ==========================

    private Boolean emailVerified;

    private Boolean phoneVerified;

    // ==========================
    // User Status
    // ==========================

    private UserStatus status;

    // ==========================
    // Audit Information
    // ==========================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}