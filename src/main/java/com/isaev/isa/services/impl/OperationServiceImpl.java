package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.OperationType;
import com.isaev.isa.exсeptions.OperationAlreadyExistException;
import com.isaev.isa.exсeptions.OperationNotFoundException;
import com.isaev.isa.repositories.OperationRepository;
import com.isaev.isa.services.OperationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Override
    public Operation checkOperation(Operation operation) {
        return operationRepository.findByOperationType(operation.getOperationType()).orElseThrow(() -> new OperationNotFoundException("Operation not found"));
    }

    @Override
    public Operation addOperation(Operation operation) {
        if (!operationRepository.existsOperationByOperationType(operation.getOperationType())) {
            operationRepository.save(operation);
            return findOperation(operation.getOperationType());
        } else {
            throw new OperationAlreadyExistException("Operation already exists");
        }
    }

    @Override
    public String deleteByOperationType(OperationType operationType) {
        if (operationRepository.existsOperationByOperationType(operationType)) {
            operationRepository.deleteOperationByOperationType(operationType);
            return (String.format("Operation with operation type - {%s} has been removed", operationType.name()));
        } else {
            throw new OperationNotFoundException(String.format("Operation with operation type - {%s} not found", operationType.name()));
        }
    }

    @Override
    public Operation findOperation(OperationType operationType){
        return operationRepository.findByOperationType(operationType).orElseThrow(() -> new OperationNotFoundException("Operation not found"));
    }

}
