package com.isaev.isa.repositories;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    boolean existsOperationByOperationType(OperationType type);

    Optional<Operation> findByOperationType(OperationType type);

    String deleteOperationByOperationType(OperationType operationType);
}
