package com.aeropelican.userservice.exceptions;

public class PhoneNumberAlreadyExistsException extends RuntimeException{
    public PhoneNumberAlreadyExistsException(String message){
        super(message);
    }
}
