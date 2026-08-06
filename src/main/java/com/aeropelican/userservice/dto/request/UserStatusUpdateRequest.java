package com.aeropelican.userservice.dto.request;

import com.aeropelican.userservice.entity.UserStatus;

public record UserStatusUpdateRequest(

        UserStatus status

) {
}