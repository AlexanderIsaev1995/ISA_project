package com.isaev.isa.dto;

import com.isaev.isa.entities.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private String number;

    public static CarDto from(Car car) {
        return CarDto.builder()
                .id(car.getId())
                .brand(car.getBrand())
                .model(car.getModel())
                .number(car.getNumber())
                .build();
    }

    public static List<CarDto> from(List<Car> cars) {
        return cars.stream().map(CarDto::from).collect(Collectors.toList());
    }

}
