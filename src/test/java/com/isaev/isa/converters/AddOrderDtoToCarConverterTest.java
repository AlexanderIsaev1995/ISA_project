package com.isaev.isa.converters;

import com.isaev.isa.converters.car.AddOrderDtoToCarConverter;
import com.isaev.isa.dto.AddOrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.exсeptions.CarNotFoundException;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.services.UserService;
import com.isaev.isa.services.impl.CarServiceImpl;
import com.isaev.isa.util.TestUtil;
import org.checkerframework.checker.units.qual.C;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AddOrderDtoToCarConverterTest {

    @InjectMocks
    private AddOrderDtoToCarConverter converter;

    @Mock
    private UserService userService;

    @Test
    public void convertWhenUserExistTest(){
        AddOrderDto addOrderDto = new AddOrderDto();
        addOrderDto.setCarNumber("test");
        addOrderDto.setCarModel("test");
        addOrderDto.setCarBrand("test");
        addOrderDto.setDescription("test");
        Car expectedCar = new Car();
        expectedCar.setNumber("test");
        expectedCar.setModel("test");
        expectedCar.setBrand("test");
        expectedCar.setOwner(TestUtil.user());
        Mockito.when(userService.getUser()).thenReturn(TestUtil.user());

        Car result = converter.convert(addOrderDto);

        Assert.assertEquals(expectedCar,result);
        Assert.assertNotNull(result);
    }

}
