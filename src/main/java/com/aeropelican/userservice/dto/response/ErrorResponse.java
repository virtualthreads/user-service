package com.aeropelican.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Application specific error code.
     * Example:
     * USER_NOT_FOUND
     * EMAIL_ALREADY_EXISTS
     * VALIDATION_ERROR
     */
    private String errorCode;

    /**
     * Human readable error message.
     */
    private String message;

    /**
     * HTTP Status Code.
     */
    private Integer status;

    /**
     * Requested API path.
     */
    private String path;

    /**
     * Validation Errors (Only for Bean Validation).
     */
    private List<FieldError> fieldErrors;

}