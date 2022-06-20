package com.isaev.isa.dtos;

import com.isaev.isa.dto.UpdateOrderDto;
import com.isaev.isa.entities.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UpdateOrderDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        UpdateOrderDto dto = new UpdateOrderDto();
        Order order = dto.newOrder();

        assertNotNull(order);
    }
}

