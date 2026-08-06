package com.aeropelican.userservice.exception;

public class RoleInUseException extends RuntimeException {

    public RoleInUseException(String message) {
        super(message);
    }
}