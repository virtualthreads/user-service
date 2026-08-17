package com.aeropelican.userservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserRoleResponseDTO {

    private UUID userRoleId;

    private UUID userId;

    private UUID roleId;

    private LocalDateTime assignedAt;


}