package com.isaev.isa.dto;

import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private String carBrand;
    private String carModel;
    private String carNumber;
    private State state;
    private String description;
    private Set<Operation> operations;
    private Double cost;
    private int pointsSpend;
    private LocalDateTime creationTime;

    public static OrderDto from(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .carBrand(order.getCar().getBrand())
                .carModel(order.getCar().getModel())
                .carNumber(order.getCar().getNumber())
                .state(order.getState())
                .description(order.getDescription())
                .operations(order.getOperationList())
                .cost(order.getCost())
                .pointsSpend(order.getPointsSpend())
                .creationTime(order.getCreationTime().toLocalDateTime())
                .build();
    }

    public static OrderDto fromRebuildOrder(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .carBrand(order.getCar().getBrand())
                .carModel(order.getCar().getModel())
                .carNumber(order.getCar().getNumber())
                .description(order.getDescription())
                .pointsSpend(order.getPointsSpend())
                .creationTime(order.getCreationTime().toLocalDateTime())
                .build();
    }


    public static Order newOrder(OrderDto o) {
        return Order.builder()
                .state(o.getState())
                .description(o.getDescription())
                .pointsSpend(o.getPointsSpend())
                .build();
    }

    public static Order rebuildOrder(OrderDto order, Car car, String description, Set<Operation> operationSet) {
        return Order.builder()
                .car(car)
                .state(order.getState())
                .description(description)
                .operationList(operationSet)
                .cost(order.getCost())
                .pointsSpend(order.getPointsSpend())
                .creationTime(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    public static List<OrderDto> from(List<Order> orders) {
        return orders.stream().map(OrderDto::from).collect(Collectors.toList());
    }
}
