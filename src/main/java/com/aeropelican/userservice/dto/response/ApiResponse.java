package com.aeropelican.userservice.dto.response;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class ApiResponse<T> {
    private  T data;
    private String message;
    private Boolean success;
    private ApiError error;
    private LocalDateTime timestamp;
}