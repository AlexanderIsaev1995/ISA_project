package com.isaev.isa.converters.car;

import com.isaev.isa.dto.AddOrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddOrderDtoToCarConverter implements Converter<AddOrderDto, Car> {
    private final UserService userService;

    @Autowired
    public AddOrderDtoToCarConverter(@Qualifier("UserServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @Override
    public Car convert(AddOrderDto orderDto) {
        User user = userService.getUser();
        Car car = new Car();
        car.setBrand(orderDto.getCarBrand());
        car.setModel(orderDto.getCarModel());
        car.setNumber(orderDto.getCarNumber());
        car.setOwner(user);
        return car;
    }
}
