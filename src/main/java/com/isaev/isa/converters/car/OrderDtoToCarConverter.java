package com.isaev.isa.converters.car;

import com.isaev.isa.dto.OrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoToCarConverter implements Converter<OrderDto, Car> {
    private final UserService userService;

    @Autowired
    public OrderDtoToCarConverter(@Qualifier("UserServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @Override
    public Car convert(OrderDto orderDto) {
        User user = userService.getUser();
        Car car = new Car();
        car.setBrand(orderDto.getCarBrand());
        car.setModel(orderDto.getCarModel());
        car.setNumber(orderDto.getCarNumber());
        car.setOwner(user);
        return car;
    }
}

