package com.isaev.isa.exсeptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
