package com.aeropelican.userservice.entity;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Table(name = "users")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {
        @Id
        @Column(name = "user_id")
        private UUID userId;

        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name cannot exceed 100 characters")
        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Size(max = 100, message = "Last name cannot exceed 100 characters")
        @Column(name = "last_name")
        private String lastName;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        @Column(name = "email", nullable = false, unique = true)
        private String email;

        @Pattern(regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit Indian mobile number")
        @Column(name = "phone_number")
        private String phoneNumber;

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
        @Column(name = "password_hash", nullable = false)
        private String passwordHash;

        @Enumerated(EnumType.STRING)
        @Column(name = "gender")
        private Gender gender;

        @PastOrPresent(message = "Date of birth cannot be a future date")
        @Column(name = "date_of_birth")
        private LocalDate dateOfBirth;

        @Builder.Default
        @Column(name = "email_verified", nullable = false)
        private Boolean emailVerified = false;

        @Builder.Default
        @Column(name = "phone_verified", nullable = false)
        private Boolean phoneVerified = false;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private UserStatus status = UserStatus.ACTIVE;

        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        @PrePersist
        public void prePersist() {
            if (userId == null) {
                userId = UUID.randomUUID();
            }
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        public void preUpdate() {
            updatedAt = LocalDateTime.now();
        }
    }
