package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRoleUpdateRequestDTO(

        @NotBlank(message = "Role Id is required")
        String roleId

) {
}