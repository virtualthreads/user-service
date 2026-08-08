package com.aeropelican.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponseDTO {

    private UUID userId;

    private UUID roleId;

    private String roleName;

    private LocalDateTime assignedAt;
}