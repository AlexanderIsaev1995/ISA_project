package com.isaev.isa.exсeptions;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String errorMessage){
        super(errorMessage);
    }
}
