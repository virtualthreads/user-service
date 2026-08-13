package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRoleCreateRequestDTO(

        @NotBlank(message = "User Id is required")
        String userId,

        @NotBlank(message = "Role Id is required")
        String roleId

) {
}