package com.isaev.isa.exceptions;

import com.isaev.isa.exсeptions.Handler;
import com.isaev.isa.exсeptions.OrderNotFoundException;
import com.isaev.isa.exсeptions.OrderStateIsNotValidException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class OrderStateNotValidExceptionTest {

    @InjectMocks
    private Handler handler;

    @Test
    public void orderStateNotValidExceptionTest(){
        ResponseEntity<OrderStateIsNotValidException> entity = new ResponseEntity<OrderStateIsNotValidException>(HttpStatus.FORBIDDEN);
        handler.validationExceptionHandler(new OrderStateIsNotValidException("State not valid"));
    }

}
