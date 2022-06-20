package com.isaev.isa.converters.car;

import com.isaev.isa.dto.UpdateOrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UpdateOrderDtoToCarConverter implements Converter<UpdateOrderDto, Car> {
    private final UserService userService;

    @Autowired
    public UpdateOrderDtoToCarConverter(@Qualifier("UserServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @Override
    public Car convert(UpdateOrderDto updateOrderDtoDto) {
        User user = userService.getUser();
        Car car = new Car();
        car.setBrand(updateOrderDtoDto.getCarBrand());
        car.setModel(updateOrderDtoDto.getCarModel());
        car.setNumber(updateOrderDtoDto.getCarNumber());
        car.setOwner(user);
        return car;
    }
}
