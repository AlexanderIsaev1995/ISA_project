package com.isaev.isa.exсeptions;

public class DiscountNotFoundException extends RuntimeException {
    public DiscountNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
