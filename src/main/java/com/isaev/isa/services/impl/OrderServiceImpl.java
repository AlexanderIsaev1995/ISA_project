package com.isaev.isa.services.impl;

import com.isaev.isa.entities.*;
import com.isaev.isa.entities.enums.State;
import com.isaev.isa.exсeptions.OrderIsAlreadyInProgressException;
import com.isaev.isa.exсeptions.OrderNotFoundException;
import com.isaev.isa.exсeptions.OrderStateIsNotValidException;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OperationRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final UserService userService;
    private final CarService carService;
    private final DiscountService discountService;
    private final OperationRepository operationRepository;
    private final OperationService operationService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CarRepository carRepository, UserRepository userRepository, ConversionService conversionService, @Qualifier("UserServiceImpl") UserService userService, CarService carService, DiscountService discountService, OperationRepository operationRepository, OperationService operationService) {
        this.orderRepository = orderRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.conversionService = conversionService;
        this.userService = userService;
        this.carService = carService;
        this.discountService = discountService;
        this.operationRepository = operationRepository;
        this.operationService = operationService;
    }

    @Override
    @Transactional
    public Order add(Order reqOrder, Car reqCar) {
        User user = userService.getUser();
        Discount discount = discountService.checkDiscount(user);
        userRepository.updateUserDiscount(discount, user.getId());
        Car car = carService.carExists(reqCar);
        Order order = Order.acceptedOrderForAdd(car, reqOrder.getDescription(), reqOrder.getPointsSpend());
        orderRepository.save(order);
        log.debug("A user with the username {} added an order.", user.getUsername());
        return order;
    }

    @Override
    @Transactional
    public Order updateByUser(Order newOrder, Car newCar, Long id) {
        Order oldOrder = getOrder(id);
        if (!oldOrder.getState().equals(State.ACCEPTED)) {
            throw new OrderIsAlreadyInProgressException("The order is already in progress");
        }
        Car newOrderCar = carService.carExists(newCar);
        carService.deleteOldCar(oldOrder.getCar().getNumber());
        if (!newOrder.getDescription().equals(oldOrder.getDescription()) && newOrder.getDescription() != null) {
            oldOrder.setDescription(newOrder.getDescription());
        }
        oldOrder.setCar(newOrderCar);
        orderRepository.save(oldOrder);
        log.debug("Update order with id - {},delete old version and save new.", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exist"));
    }

    @Override
    public Order updateOperations(Long id, Set<Operation> operations) {
        Set<Operation> operationSet = new HashSet<Operation>();
        for (Operation operation : operations) {
            operationSet.add(operationService.checkOperation(operation));
        }
        Order newOrder = getOrder(id);
        newOrder.setOperationList(operationSet);
        orderRepository.save(newOrder);
        log.debug("Update operations in order with id - {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exists"));
    }

    @Override
    @Transactional
    public Order updateState(Long id, String state) {
        orderRepository.updateState(getState(state), id);
        log.debug("Update state in order with id - {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exists"));
    }

    @Override
    @Transactional
    public Order updateCost(Long id, double cost) {
        User user = userService.getUser();
        Order order = getOrder(id);
        double finallyCost = cost * user.getDiscount().getCoefficientDiscount();
        try {
            if (user.getBonusPoints() != 0 && order.getPointsSpend() <= user.getBonusPoints()) {
                finallyCost -= order.getPointsSpend();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        double bonusPoints = finallyCost * 0.1;
        orderRepository.updateCost(finallyCost, id);
        userRepository.updateUserBonusPoints(bonusPoints, user.getId());
        log.debug("Update cost in order with id - {}. Adding bonuses to a user with a username - {}.", id, user.getUsername());
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exists"));
    }

    @Override
    public Order readOne(Long id) {
        log.debug("Read one order with id - {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exists"));
    }

    @Override
    public Order readOneService(Long id) {
        log.debug("Read one service order with id - {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("No such order exists"));
    }

    @Override
    public List<Order> readAll() {
        log.debug("Read all service orders");
        return orderRepository.findAll();
    }

    @Override
    public List<Order> readAllByCarOwnerId() {
        Long id = userService.getUser().getId();
        log.debug("Read all user orders - {}", userService.getUser().getUsername());
        return orderRepository.findAllByCarOwnerId(id);
    }

    @Override
    public List<Order> readAllState(String state) {
        log.debug("Read all orders with state - {}", state);
        switch (State.valueOf(state)) {
            case ACCEPTED:
                return new ArrayList<>(orderRepository.findAllByState(State.ACCEPTED));
            case IN_WORK:
                return new ArrayList<>(orderRepository.findAllByState(State.IN_WORK));
            case COMPLETED:
                return new ArrayList<>(orderRepository.findAllByState(State.COMPLETED));
        }
        throw new OrderStateIsNotValidException("Please enter valid state");
    }

    @Override
    public Order rebuildOrder(Order newOrder, Order oldOrder, Car car) {
        String description;
        int pointsSpend;
        if (newOrder.getDescription() != null) {
            description = newOrder.getDescription();
        } else description = oldOrder.getDescription();
        if (newOrder.getPointsSpend() != 0) {
            pointsSpend = newOrder.getPointsSpend();
        } else {
            pointsSpend = oldOrder.getPointsSpend();
        }
        log.debug("Rebuild order with id - {}", oldOrder.getId());
        return Order.rebuildOrder(oldOrder, car, description, pointsSpend);
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public State getState(String state) {
        log.debug("Getting state with String state - {}", state);
        return State.valueOf(state.toUpperCase());
    }

    public Order getOrder(Long id) {
        log.debug("Getting order with id - {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
}
