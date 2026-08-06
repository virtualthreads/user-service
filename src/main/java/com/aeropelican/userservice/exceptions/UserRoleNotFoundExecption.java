package com.aeropelican.userservice.exceptions;

public class UserRoleNotFoundExecption extends RuntimeException {
    public UserRoleNotFoundExecption(String message) {
        super(message);
    }
}
