package com.isaev.isa.dtos;

import com.isaev.isa.dto.AddOrderDto;
import com.isaev.isa.entities.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AddOrderDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        AddOrderDto dto = new AddOrderDto();
        Order order = dto.toCreateOrder();

        assertNotNull(order);
    }
}

