package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public class UserStatusUpdateRequestDTO {
    @NotNull
    private UserStatus status;


}
