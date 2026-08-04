package com.aeropelican.userservice.entity;

import com.aeropelican.userservice.enums.Gender;
import com.aeropelican.userservice.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(
            name = "user_id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private UUID userId;

    @Column(
            name = "first_name",
            length = 100,
            nullable = false
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 100
    )
    private String lastName;

    @Column(
            name = "email",
            length = 255,
            nullable = false,
            unique = true
    )
    private String email;

    @Column(
            name = "phone_number",
            length = 20
    )
    private String phoneNumber;

    @Column(
            name = "password_hash",
            length = 255,
            nullable = false
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender"
    )
    private Gender gender;

    @Column(
            name = "date_of_birth"
    )
    private LocalDate dateOfBirth;

    @Column(
            name = "email_verified",
            nullable = false
    )
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(
            name = "phone_verified",
            nullable = false
    )
    @Builder.Default
    private Boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (emailVerified == null) {
            emailVerified = false;
        }

        if (phoneVerified == null) {
            phoneVerified = false;
        }

        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}