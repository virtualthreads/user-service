package com.aeropelican.userservice.exceptions;

public class AddressOwnershipException extends RuntimeException {
    public AddressOwnershipException(String message) {
        super(message);
    }
}