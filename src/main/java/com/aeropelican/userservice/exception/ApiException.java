package com.aeropelican.userservice.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
