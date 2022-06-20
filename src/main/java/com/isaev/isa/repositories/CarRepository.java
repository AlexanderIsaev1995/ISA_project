package com.isaev.isa.repositories;

import com.isaev.isa.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsCarByNumber(String number);

    Optional<Car> findById(Long id);

    Optional<Car> findByNumber(String number);
}
