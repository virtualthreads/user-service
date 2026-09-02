package com.aeropelican.userservice.exceptions;

import com.aeropelican.userservice.dto.response.ApiError;
import com.aeropelican.userservice.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            UserNotFound ex,
            HttpServletRequest request) {
        log.error("Class not found exception occured:");
        ApiError error = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .error(error)
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);


    }

    @ExceptionHandler(DuplicateResource.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(
            DuplicateResource ex,
            HttpServletRequest request) {
        log.error("Duplicate resource: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .error("DUPLICATE_RESOURCE")
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(ex.getMessage())
                .error(apiError)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(MappingAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleMappingAlreadyExists(
            MappingAlreadyExistsException ex,
            HttpServletRequest request) {

        log.error("{}", ex.getMessage());

        ApiError apiError = ApiError.builder()
                .error("MAPPING_ALREADY_EXISTS")
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .error(apiError)
                        .build());
    }


}
