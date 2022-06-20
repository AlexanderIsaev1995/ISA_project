package com.isaev.isa.entities;

import com.isaev.isa.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "order_id"))})
public class Order extends BaseEntity {

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "car_id")
    private Car car;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "orders_operations",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "operation_id", referencedColumnName = "operation_id")})
    private Set<Operation> operationList;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "points_spend")
    private int pointsSpend;

    public static Order acceptedOrderForAdd(Car newCar, String description, int pointsSpend) {
        return Order.builder()
                .car(newCar)
                .state(State.ACCEPTED)
                .description(description)
                .creationTime(Timestamp.valueOf(LocalDateTime.now()))
                .pointsSpend(pointsSpend)
                .build();
    }

    public static Order rebuildOrder(Order order, Car car, String description, int pointsSpend) {
        return Order.builder()
                .car(car)
                .state(order.getState())
                .description(description)
                .pointsSpend(pointsSpend)
                .build();
    }
}
