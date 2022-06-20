package com.isaev.isa.dtos;

import com.isaev.isa.dto.OperationDto;
import com.isaev.isa.entities.Operation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OperationDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        OperationDto operationDto = new OperationDto();
        operationDto.setOperationType("ENGINE_FULL");
        operationDto.setDescription("test");
        operationDto.setCost(100);
        Operation operation = operationDto.toOperation();
        OperationDto operationDtoNew = OperationDto.fromOperation(operation);

        assertNotNull(operation);
        assertNotNull(operationDtoNew);
    }
}

