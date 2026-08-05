package com.aeropelican.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleUpdateRequestDTO {

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;
}
