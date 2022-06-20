package com.isaev.isa.exсeptions;

public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
