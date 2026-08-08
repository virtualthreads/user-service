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

    // =========================================================
    // USER NOT FOUND
    // =========================================================

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

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // EMAIL ALREADY EXISTS
    // =========================================================

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

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // PHONE NUMBER ALREADY EXISTS
    // =========================================================

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

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // VALIDATION ERROR
    // =========================================================

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

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // =========================================================
    // BINDING VALIDATION ERROR
    // =========================================================

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

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // =========================================================
    // ROLE NOT FOUND
    // =========================================================

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
            RoleNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                "Role not found: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ROLE_NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // ROLE ALREADY EXISTS
    // =========================================================

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExistsException(
            RoleAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn(
                "Duplicate role: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ROLE_ALREADY_EXISTS")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // ROLE IN USE
    // =========================================================

    @ExceptionHandler(RoleInUseException.class)
    public ResponseEntity<ErrorResponse> handleRoleInUseException(
            RoleInUseException ex,
            HttpServletRequest request) {

        log.warn(
                "Role is in use: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ROLE_IN_USE")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // USER ROLE ALREADY EXISTS
    // =========================================================

    @ExceptionHandler(UserRoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserRoleAlreadyExistsException(
            UserRoleAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn(
                "User-role mapping already exists: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("USER_ROLE_ALREADY_EXISTS")
                .message(ex.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // =========================================================
    // USER ROLE NOT FOUND
    // =========================================================

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserRoleNotFoundException(
            UserRoleNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                "User-role mapping not found: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("USER_ROLE_NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // ADDRESS NOT FOUND
    // =========================================================

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAddressNotFoundException(
            AddressNotFoundException ex,
            HttpServletRequest request) {

        log.warn(
                "Address not found: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("ADDRESS_NOT_FOUND")
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // INVALID REQUEST
    // =========================================================

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(
            InvalidRequestException ex,
            HttpServletRequest request) {

        log.warn(
                "Invalid request: {}",
                ex.getMessage()
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("INVALID_REQUEST")
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // =========================================================
    // UNEXPECTED EXCEPTION
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request) {

        log.error(
                "Unexpected exception occurred",
                ex
        );

        ErrorResponse response = ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message(
                        "Something went wrong. " +
                                "Please contact administrator."
                )
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}