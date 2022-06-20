package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Car;
import com.isaev.isa.exсeptions.CarNotFoundException;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CarRepository carRepository;

    @Test
    public void carExistWhenCarNotExistTest(){
        Car car = TestUtil.car();
        Car expectedCar = TestUtil.car();

        Mockito.when(carRepository.existsCarByNumber(car.getNumber())).thenReturn(false);
        Mockito.when(carRepository.findByNumber(car.getNumber())).thenReturn(Optional.of(expectedCar));
        Car result = carService.carExists(car);

        Assert.assertSame(expectedCar,result);
        Assert.assertNotNull(result);

        Mockito.verify(carRepository,Mockito.times(1)).existsCarByNumber(car.getNumber());
        Mockito.verify(carRepository,Mockito.times(1)).findByNumber(expectedCar.getNumber());
    }

    @Test(expected = CarNotFoundException.class)
    public void carExistWhenCarExist() {
        Car car = TestUtil.car();
        Mockito.when(carRepository.existsCarByNumber(car.getNumber())).thenReturn(true);
        Car newCar = carService.carExists(car);

        Mockito.verify(carRepository, Mockito.times(1)).existsCarByNumber(car.getNumber());
    }



}
