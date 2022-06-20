package com.isaev.isa.dto;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationDto {
    private String operationType;
    private String description;
    private int cost;

    public Operation toOperation() {
        return Operation.builder()
                .operationType(OperationType.valueOf(operationType.toUpperCase()))
                .operationDescription(description)
                .cost(cost)
                .build();
    }

    public static OperationDto fromOperation(Operation operation) {
        return OperationDto.builder()
                .operationType(operation.getOperationType().name())
                .description(operation.getOperationDescription())
                .cost(operation.getCost())
                .build();
    }
}
