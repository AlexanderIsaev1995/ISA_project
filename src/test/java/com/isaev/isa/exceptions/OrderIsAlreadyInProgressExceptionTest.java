package com.isaev.isa.exceptions;

import com.isaev.isa.exсeptions.Handler;
import com.isaev.isa.exсeptions.OrderIsAlreadyInProgressException;
import com.isaev.isa.exсeptions.OrderStateIsNotValidException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class OrderIsAlreadyInProgressExceptionTest {

    @InjectMocks
    private Handler handler;

    @Test
    public void orderIsAlreadyInProgressExceptionTest(){
        ResponseEntity<OrderIsAlreadyInProgressException> entity = new ResponseEntity<OrderIsAlreadyInProgressException>(HttpStatus.FORBIDDEN);
        handler.validationExceptionHandler(new OrderIsAlreadyInProgressException("Order already in progress"));
    }

}
