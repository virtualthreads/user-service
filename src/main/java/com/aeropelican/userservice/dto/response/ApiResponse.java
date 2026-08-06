package com.aeropelican.userservice.dto.response;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    @SuppressWarnings("unused")
    public boolean isSuccess() { return success; }

    @SuppressWarnings("unused")
    public String getMessage() { return message; }

    @SuppressWarnings("unused")
    public T getData() { return data; }

    @SuppressWarnings("unused")
    public LocalDateTime getTimestamp() { return timestamp; }
}