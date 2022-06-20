package com.isaev.isa.exceptions;

import com.isaev.isa.entities.Car;
import com.isaev.isa.exсeptions.CarNotFoundException;
import com.isaev.isa.exсeptions.Handler;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.util.TestUtil;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class HandlerTest {

    @InjectMocks
    private Handler handler;

    @Test
    public void handlerTest(){
        ResponseEntity<CarNotFoundException> entity = new ResponseEntity<CarNotFoundException>(HttpStatus.FORBIDDEN);
        handler.validationExceptionHandler(new CarNotFoundException("Car not found"));
    }

}
