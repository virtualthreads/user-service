package com.aeropelican.userservice.dto.Request;

import com.aeropelican.userservice.entity.enums.UserStatus;

public record UserStatusUpdateRequest(UserStatus status) {}