package com.aeropelican.userservice.exceptions;

public class MappingAlreadyExistsException extends RuntimeException{
    public MappingAlreadyExistsException(String message){
        super(message);
    }
}
