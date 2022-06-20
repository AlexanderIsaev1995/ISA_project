package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.OperationType;
import com.isaev.isa.exсeptions.OperationAlreadyExistException;
import com.isaev.isa.exсeptions.OperationNotFoundException;
import com.isaev.isa.repositories.OperationRepository;
import com.isaev.isa.services.impl.OperationServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceImplTest {

    @InjectMocks
    private OperationServiceImpl operationService;

    @Mock
    private OperationRepository operationRepository;

    @Test
    public void checkOperationWhenOperationExistTest(){
        Operation operation = new Operation();
        operation.setOperationType(OperationType.ENGINE_FULL);
        Operation expectedOperation = new Operation(OperationType.ENGINE_FULL,"test",100);

        Mockito.when(operationRepository.findByOperationType(operation.getOperationType())).thenReturn(Optional.of(expectedOperation));
        Operation resultOperation = operationService.checkOperation(operation);

        Assert.assertSame(expectedOperation,resultOperation);
        Assert.assertNotNull(resultOperation);

        Mockito.verify(operationRepository,Mockito.times(1)).findByOperationType(operation.getOperationType());
    }

    @Test
    public void checkOperationWhenOperationNotExistTest(){
        Operation operation = new Operation();
        operation.setOperationType(OperationType.ENGINE_FULL);
        try{
            Operation resultOperation = operationService.checkOperation(operation);
        } catch (OperationNotFoundException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addOperationWhenOperationNotExist(){
        Operation operation = new Operation();
        operation.setOperationType(OperationType.ENGINE_FULL);
        Operation expectedOperation = new Operation(OperationType.ENGINE_FULL,"test",100);

        Mockito.when(operationRepository.existsOperationByOperationType(operation.getOperationType())).thenReturn(false);
        Mockito.when(operationRepository.findByOperationType(operation.getOperationType())).thenReturn(Optional.of(expectedOperation));
        Operation resultOperation = operationService.addOperation(operation);

        Assert.assertSame(expectedOperation,resultOperation);
        Assert.assertNotNull(resultOperation);

        Mockito.verify(operationRepository,Mockito.times(1)).existsOperationByOperationType(operation.getOperationType());
        Mockito.verify(operationRepository,Mockito.times(1)).findByOperationType(operation.getOperationType());
    }

    @Test
    public void addOperationWhenOperationExist(){
        Operation operation = new Operation();
        operation.setOperationType(OperationType.ENGINE_FULL);

        Mockito.when(operationRepository.existsOperationByOperationType(operation.getOperationType())).thenReturn(true);
        try{
            Operation resultOperation = operationService.addOperation(operation);
        } catch (OperationAlreadyExistException e){
            e.printStackTrace();
        }

        Mockito.verify(operationRepository,Mockito.times(1)).existsOperationByOperationType(operation.getOperationType());
    }

    @Test
    public void deleteByOperationTypeWhenOperationExist(){
        OperationType operationType = OperationType.ENGINE_FULL;
        String expectedString = String.format("Operation with operation type - {%s} has been removed", operationType.name());

        Mockito.when(operationRepository.existsOperationByOperationType(operationType)).thenReturn(true);

        String resultString = operationService.deleteByOperationType(operationType);

        Assert.assertEquals(expectedString,resultString);
        Assert.assertNotNull(resultString);

        Mockito.verify(operationRepository,Mockito.times(1)).existsOperationByOperationType(operationType);
    }

    @Test
    public void deleteByOperationTypeWhenOperationNotExist() {
        OperationType operationType = OperationType.ENGINE_FULL;

        try {
            String resultString = operationRepository.deleteOperationByOperationType(operationType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findOperationWhenOperationExist(){
        OperationType operationType = OperationType.ENGINE_FULL;
        Operation expectedOperation = new Operation(OperationType.ENGINE_FULL,"test",100);

        Mockito.when(operationRepository.findByOperationType(operationType)).thenReturn(Optional.of(expectedOperation));

        Operation resultOperation = operationService.findOperation(operationType);

        Assert.assertSame(expectedOperation,resultOperation);
        Assert.assertNotNull(resultOperation);

        Mockito.verify(operationRepository,Mockito.times(1)).findByOperationType(operationType);
    }

    @Test
    public void findOperationWhenOperationNotExist(){
        OperationType operationType = OperationType.ENGINE_FULL;

        try{
            Operation resultOperation = operationService.findOperation(operationType);
        } catch (OperationNotFoundException e){
            e.printStackTrace();
        }
    }
}
