package com.isaev.isa.dto;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDtoService {
    private Long id;
    private Long orderId;
    private String userFirstName;
    private String userLastName;
    private String carBrand;
    private String carNumber;
    private String carModel;
    private State state;
    private String description;
    private Set<Operation> operations;
    private Double cost;
    private int pointsSpend;
    private LocalDateTime creationTime;

    public static OrderDtoService from(Order order) {
        return OrderDtoService.builder()
                .id(order.getCar().getOwner().getId())
                .orderId(order.getId())
                .userFirstName(order.getCar().getOwner().getFirstName())
                .userLastName(order.getCar().getOwner().getLastName())
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

    public static OrderDtoService from(Order order, Set<Operation> operationSet) {
        return OrderDtoService.builder()
                .id(order.getCar().getOwner().getId())
                .userFirstName(order.getCar().getOwner().getFirstName())
                .userLastName(order.getCar().getOwner().getLastName())
                .carBrand(order.getCar().getBrand())
                .carModel(order.getCar().getModel())
                .carNumber(order.getCar().getNumber())
                .state(order.getState())
                .description(order.getDescription())
                .operations(operationSet)
                .cost(order.getCost())
                .pointsSpend(order.getPointsSpend())
                .creationTime(order.getCreationTime().toLocalDateTime())
                .build();
    }

    public static List<OrderDtoService> from(List<Order> orders) {
        return orders.stream().map(OrderDtoService::from).collect(Collectors.toList());
    }

}
