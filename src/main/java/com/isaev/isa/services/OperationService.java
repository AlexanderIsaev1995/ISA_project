package com.isaev.isa.services;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.OperationType;

public interface OperationService {

    Operation checkOperation(Operation operation);

    Operation addOperation(Operation operation);

    String deleteByOperationType(OperationType operationType);

    Operation findOperation(OperationType operationType);
}
