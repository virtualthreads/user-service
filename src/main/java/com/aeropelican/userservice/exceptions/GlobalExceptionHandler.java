package com.aeropelican.userservice.exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.aeropelican.userservice.dto.response.ApiError;
import com.aeropelican.userservice.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        @ExceptionHandler(RoleAssignedException.class)
        public ResponseEntity<ApiResponse<Void>> handleRoleAssigned(RoleAssignedException ex) {
            log.error("Already assigned role");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message(ex.getMessage())
                            .build());
        }
        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(
                DuplicateResourceException ex,
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
    }