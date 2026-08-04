package com.aeropelican.userservice.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.List;
@Data
@Builder
public class ApiResponse<T> {
        private  T data;
        private String message;
        private Boolean success;
        private ApiError error;
    }

