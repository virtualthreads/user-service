package com.aeropelican.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private ErrorResponse error;

    private LocalDateTime timestamp;

    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> APIResponse<T> failure(
            String message,
            ErrorResponse error) {

        return APIResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}