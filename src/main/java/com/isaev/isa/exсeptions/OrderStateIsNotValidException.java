package com.isaev.isa.exсeptions;

public class OrderStateIsNotValidException extends RuntimeException {
    public OrderStateIsNotValidException(String errorMessage){
        super(errorMessage);
    }
}
