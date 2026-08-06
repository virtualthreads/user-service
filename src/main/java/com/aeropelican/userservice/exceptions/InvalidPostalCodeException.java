package com.aeropelican.userservice.exceptions;

public class InvalidPostalCodeException extends RuntimeException {
    public InvalidPostalCodeException(String message) {
        super(message);
    }
}
