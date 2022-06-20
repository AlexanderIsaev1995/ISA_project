package com.isaev.isa.exсeptions;

public class DiscountAlreadyExistException extends RuntimeException {
    public DiscountAlreadyExistException(String errorMessage){
        super(errorMessage);
    }
}
