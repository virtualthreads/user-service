package com.aeropelican.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class RoleResponseDTO {
    private UUID roleId;
    private String roleName;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
