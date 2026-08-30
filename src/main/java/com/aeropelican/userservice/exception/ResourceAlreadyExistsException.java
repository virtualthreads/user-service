package com.aeropelican.userservice.exception;

public class ResourceAlreadyExistsException extends ApiException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
