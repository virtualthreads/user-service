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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id", updatable = false, nullable = false,length = 36)
    private UUID userId;
    @Column(name="first_name", nullable=false, length=100)
    private String firstName;
    @Column(name = "last_name",nullable=false, length=100)
    private String lastName;
    @Column(name = "email", unique = true,nullable=false, length=100)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password_hash",nullable=false, length=100)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @PastOrPresent(message = "Date of birth cannot be a future date")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Builder.Default
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Builder.Default
    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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