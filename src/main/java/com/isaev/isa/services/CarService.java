package com.isaev.isa.services;

import com.isaev.isa.entities.Car;

public interface CarService {

    Car carExists(Car car);

    Car getCar(String number);

    void deleteOldCar(String number);
}
