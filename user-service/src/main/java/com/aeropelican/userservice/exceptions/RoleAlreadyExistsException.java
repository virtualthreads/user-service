package com.aeropelican.userservice.exceptions;

public class RoleAlreadyExistsException extends RuntimeException{
    public RoleAlreadyExistsException(String message){
        super(message);
    }
}
