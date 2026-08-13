package com.aeropelican.userservice.dto.response;

import java.time.LocalDateTime;

public record ApiResponse(

        boolean success,
        String message,
        Object data

) {

}