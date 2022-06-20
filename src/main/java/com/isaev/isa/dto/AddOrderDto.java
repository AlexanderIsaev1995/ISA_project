package com.isaev.isa.dto;

import com.isaev.isa.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddOrderDto {
    private String carBrand;
    private String carModel;
    private String carNumber;
    private String description;
    private int pointsSpend;

    public Order toCreateOrder() {
        return Order.builder()
                .description(description)
                .pointsSpend(pointsSpend)
                .build();
    }
}
