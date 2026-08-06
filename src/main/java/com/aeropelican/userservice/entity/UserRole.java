package com.aeropelican.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @UuidGenerator
    @Column(
            name = "user_role_id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private UUID userRoleId;

    @Column(
            name = "user_id",
            length = 36,
            nullable = false
    )
    private UUID userId;

    @Column(
            name = "role_id",
            length = 36,
            nullable = false
    )
    private UUID roleId;

    @Column(
            name = "assigned_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {

        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }
}