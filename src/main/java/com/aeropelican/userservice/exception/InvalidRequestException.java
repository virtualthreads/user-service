package com.aeropelican.userservice.exception;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
