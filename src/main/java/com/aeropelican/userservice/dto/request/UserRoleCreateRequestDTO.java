package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserRoleCreateRequestDTO {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Role ID is required")
    private UUID roleId;
}