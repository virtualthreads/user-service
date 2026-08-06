package com.aeropelican.userservice.exception;

import com.aeropelican.userservice.dto.response.ErrorResponse;
import com.aeropelican.userservice.dto.response.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request) {

        log.warn("User not found: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("USER_NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn("Duplicate email: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("EMAIL_ALREADY_EXISTS")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePhoneNumberAlreadyExistsException(
            PhoneNumberAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn("Duplicate phone number: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("PHONE_NUMBER_ALREADY_EXISTS")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Validation failed");

        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("VALIDATION_ERROR")
                .message("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex,
            HttpServletRequest request) {

        log.warn("Binding validation failed");

        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> FieldError.builder()
                        .field(error.getField())
                        .rejectedValue(error.getRejectedValue())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("VALIDATION_ERROR")
                .message("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected exception occurred", ex);

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("Something went wrong. Please contact administrator.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
            RoleNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                "Role not found: {}",
                ex.getMessage()
        );

        ErrorResponse response =
                ErrorResponse.builder()
                        .errorCode("ROLE_NOT_FOUND")
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .path(request.getRequestURI())
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExistsException(
            RoleAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn(
                "Duplicate role: {}",
                ex.getMessage()
        );

        ErrorResponse response =
                ErrorResponse.builder()
                        .errorCode("ROLE_ALREADY_EXISTS")
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT.value())
                        .path(request.getRequestURI())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    @ExceptionHandler(RoleInUseException.class)
    public ResponseEntity<ErrorResponse> handleRoleInUseException(
            RoleInUseException ex,
            HttpServletRequest request) {

        log.warn(
                "Role is in use: {}",
                ex.getMessage()
        );

        ErrorResponse response =
                ErrorResponse.builder()
                        .errorCode("ROLE_IN_USE")
                        .message(ex.getMessage())
                        .status(HttpStatus.CONFLICT.value())
                        .path(request.getRequestURI())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

}