package com.isaev.isa.exceptions;

import com.isaev.isa.exсeptions.CarNotFoundException;
import com.isaev.isa.exсeptions.Handler;
import com.isaev.isa.exсeptions.OrderNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class OrderNotFoundExceptionTest {

    @InjectMocks
    private Handler handler;

    @Test
    public void orderNotFoundExceptionTest(){
        ResponseEntity<OrderNotFoundException> entity = new ResponseEntity<OrderNotFoundException>(HttpStatus.FORBIDDEN);
        handler.validationExceptionHandler(new OrderNotFoundException("Order not found"));
    }

}
