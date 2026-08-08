package com.aeropelican.userservice.exception;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}