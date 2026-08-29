package com.aeropelican.userservice.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j

public class UserNotFound extends RuntimeException{
    public UserNotFound (String message){
        super(message);
        log.error("User Not Found");
    }
}
