package com.isaev.isa.services;

import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.Order;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Order add(Order o, Car car);

    Order updateByUser(Order o, Car car, Long id);

    Order updateOperations(Long id, Set<Operation> operations);

    Order updateState(Long id, String state);

    Order updateCost(Long id, double cost);

    Order readOne(Long id);

    Order readOneService(Long id);

    List<Order> readAll();

    List<Order> readAllState(String state);

    List<Order> readAllByCarOwnerId();

    Order rebuildOrder(Order newOrder, Order oldOrder, Car car);

    void delete(Long id);

}
