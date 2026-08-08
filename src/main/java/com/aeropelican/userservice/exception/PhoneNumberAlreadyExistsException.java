package com.aeropelican.userservice.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {

    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }

}