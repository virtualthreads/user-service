package com.aeropelican.userservice.exceptions;

public class ResourceNotFound  extends RuntimeException{
    public ResourceNotFound (String message){
        super(message);
    }
}
