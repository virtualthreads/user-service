package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;
@Data
public class AssignRoleRequestDTO {

    @NotNull(message = "Role Id is required")
    private UUID roleId;


}