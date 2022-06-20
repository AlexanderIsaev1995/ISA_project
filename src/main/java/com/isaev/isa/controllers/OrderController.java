package com.isaev.isa.controllers;

import com.isaev.isa.dto.*;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.Order;
import com.isaev.isa.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasAuthority('AddOneOrder')")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class OrderController {

    private final OrderService orderService;
    private final ConversionService convServ;

    @Autowired
    public OrderController(OrderService orderService, ConversionService convServ) {
        this.orderService = orderService;
        this.convServ = convServ;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody AddOrderDto request) {
        Car car = convServ.convert(request, Car.class);
        Order order = request.toCreateOrder();
        log.debug("Request to create order was sent");
        return ResponseEntity.ok(OrderDto.from(orderService.add(order, car)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        log.debug("Request to get order by id - {} was sent", id);
        return ResponseEntity.ok(OrderDto.from(orderService.readOne(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> updateById(@PathVariable Long id, @RequestBody UpdateOrderDto o) {
        Order newOrder = o.newOrder();
        Car newCar = convServ.convert(o, Car.class);
        log.debug("Request to update order with id - {} was sent", id);
        return ResponseEntity.ok(OrderDto.fromRebuildOrder(orderService.updateByUser(newOrder, newCar, id)));
    }

    @GetMapping
    public List<OrderDto> getAll() {
        log.debug("Request to get all user orders was sent");
        return OrderDto.from(orderService.readAllByCarOwnerId());
    }

    @GetMapping("/service")
    @PreAuthorize("hasAuthority('ReadAllOrders')")
    public List<OrderDtoService> readAll() {
        log.debug("Request to get all orders was sent");
        return OrderDtoService.from(orderService.readAll());
    }

    @GetMapping("/service/{state}")
    @PreAuthorize("hasAuthority('ReadAllOrders')")
    public List<OrderDtoService> readAllState(@PathVariable String state) {
        log.debug("Request to get all orders with state - {} was sent", state);
        return OrderDtoService.from(orderService.readAllState(state));
    }

    @PatchMapping("service/states/{id}")
    @PreAuthorize("hasAuthority('ReadAllOrders')")
    public ResponseEntity<OrderDto> updateState(@PathVariable Long id, @RequestBody UpdateStateDto state) {
        log.debug("Request to update orders - {} state - {} was sent", id, state.getValue());
        return ResponseEntity.ok(OrderDto.from(orderService.updateState(id, state.getValue())));
    }

    @PatchMapping("service/operations/{id}")
    @PreAuthorize("hasAuthority('ReadAllOrders')")
    public ResponseEntity<OrderDto> updateOperations(@PathVariable Long id, @RequestBody Set<OperationDto> operationSet) {
        Set<Operation> operationsForOrder = new HashSet<Operation>();
        for (OperationDto dto : operationSet) {
            operationsForOrder.add(dto.toOperation());
        }
        return ResponseEntity.ok(OrderDto.from(orderService.updateOperations(id, operationsForOrder)));
    }


    @PatchMapping("service/costs/{id}")
    @PreAuthorize("hasAuthority('ReadAllOrders')")
    public ResponseEntity<OrderDtoService> updateCost(@PathVariable Long id, @RequestBody UpdateCostDto updateCostDto) {
        log.debug("Request to update orders - {} cost was sent");
        return ResponseEntity.ok(OrderDtoService.from(orderService.updateCost(id, updateCostDto.getCost())));
    }
}
