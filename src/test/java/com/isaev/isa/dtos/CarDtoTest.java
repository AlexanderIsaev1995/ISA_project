package com.isaev.isa.dtos;

import com.isaev.isa.dto.CarDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CarDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        Car car = TestUtil.car();
        Car carNew = TestUtil.car();
        List<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(carNew);
        CarDto carDto = CarDto.from(car);
        List<CarDto> carDtos = CarDto.from(cars);

        assertNotNull(carDto);
        assertNotNull(carDtos);
    }
}

