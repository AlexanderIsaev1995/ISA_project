package com.isaev.isa.converters;

import com.isaev.isa.converters.car.AddOrderDtoToCarConverter;
import com.isaev.isa.converters.car.OrderDtoToCarConverter;
import com.isaev.isa.dto.AddOrderDto;
import com.isaev.isa.dto.OrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.services.UserService;
import com.isaev.isa.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderDtoToCarConverterTest {

    @InjectMocks
    private OrderDtoToCarConverter converter;

    @Mock
    private UserService userService;

    @Test
    public void convertWhenUserExistTest(){
        OrderDto orderDto = new OrderDto();
        orderDto.setCarNumber("test");
        orderDto.setCarModel("test");
        orderDto.setCarBrand("test");
        orderDto.setDescription("test");
        Car expectedCar = new Car();
        expectedCar.setNumber("test");
        expectedCar.setModel("test");
        expectedCar.setBrand("test");
        expectedCar.setOwner(TestUtil.user());
        Mockito.when(userService.getUser()).thenReturn(TestUtil.user());

        Car result = converter.convert(orderDto);

        Assert.assertEquals(expectedCar,result);
        Assert.assertNotNull(result);
    }

}
