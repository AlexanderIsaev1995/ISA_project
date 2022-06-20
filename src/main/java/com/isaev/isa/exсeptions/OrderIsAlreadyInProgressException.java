package com.isaev.isa.exсeptions;

public class OrderIsAlreadyInProgressException extends RuntimeException {
    public OrderIsAlreadyInProgressException(String errorMessage){
        super(errorMessage);
    }
}
