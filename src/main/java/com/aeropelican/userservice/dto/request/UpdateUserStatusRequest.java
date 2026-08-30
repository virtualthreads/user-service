package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "Status is mandatory") Status status
) {}
