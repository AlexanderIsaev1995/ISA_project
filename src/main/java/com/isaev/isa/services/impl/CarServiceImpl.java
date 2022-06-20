package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Car;
import com.isaev.isa.exсeptions.CarNotFoundException;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.services.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ConversionService conversionService;

    @Override
    public Car carExists(Car car) {
        if (!carRepository.existsCarByNumber(car.getNumber()) && car != null) {
            log.debug("Car with number - {} added", car.getNumber());
            carRepository.save(car);
        }
        return getCar(car.getNumber());
    }

    @Override
    public Car getCar(String number) {
        return carRepository.findByNumber(number).orElseThrow(() -> new CarNotFoundException("Car not found"));
    }

    @Override
    public void deleteOldCar(String number) {
        Car oldCar = getCar(number);
        carRepository.deleteById(oldCar.getId());
    }
}
