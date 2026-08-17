package com.aeropelican.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    @Column(name = "user_role_id", nullable = false,length = 36)
    private UUID userRoleId;
    @Column(name = "user_id", nullable = false,length = 36)
    private UUID userId;
    @Column(name = "role_id", nullable = false,length = 36)
    private UUID roleId;
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    public void prePersist() {
        if (userRoleId == null) {
            userRoleId = UUID.randomUUID();
        }
        assignedAt = LocalDateTime.now();
    }
}