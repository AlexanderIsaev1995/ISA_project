package com.isaev.isa.exсeptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
