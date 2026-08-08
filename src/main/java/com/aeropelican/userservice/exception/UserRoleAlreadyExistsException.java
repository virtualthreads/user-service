package com.aeropelican.userservice.exception;

public class UserRoleAlreadyExistsException extends RuntimeException {

    public UserRoleAlreadyExistsException(String message) {
        super(message);
    }
}