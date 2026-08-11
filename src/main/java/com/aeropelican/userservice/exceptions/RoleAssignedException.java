package com.aeropelican.userservice.exceptions;

public class RoleAssignedException extends RuntimeException {
    public RoleAssignedException(String message)
    {
        super(message);
    }
}
