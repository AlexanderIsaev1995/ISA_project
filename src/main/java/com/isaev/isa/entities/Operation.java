package com.isaev.isa.entities;

import com.isaev.isa.entities.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "operation_id"))})
public class Operation extends BaseEntity {

    @Column(name = "operation_type")
    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;

    @Column(name = "description")
    private String operationDescription;

    @Column(name = "cost")
    private int cost;

}
