package com.aeropelican.userservice.exceptions;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String message){
        super(message);
    }
}
