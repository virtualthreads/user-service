package com.aeropelican.userservice.exception;

public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(message);
    }
}
