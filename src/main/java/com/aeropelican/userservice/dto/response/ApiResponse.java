package com.aeropelican.userservice.dto.response;

import java.time.LocalDateTime;

public record ApiResponse(

        LocalDateTime timestamp,
        Integer status,
        String message

) {
}