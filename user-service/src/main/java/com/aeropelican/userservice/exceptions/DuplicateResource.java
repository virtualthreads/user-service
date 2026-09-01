package com.aeropelican.userservice.exceptions;

public class DuplicateResource extends RuntimeException{
    public DuplicateResource(String message){
        super(message);
    }
}
