package com.isaev.isa.exсeptions;

public class OperationAlreadyExistException extends RuntimeException {
    public OperationAlreadyExistException(String errorMessage){
        super(errorMessage);
    }
}
