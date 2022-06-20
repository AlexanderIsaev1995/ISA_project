package com.isaev.isa.dtos;

import com.isaev.isa.dto.OrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.State;
import com.isaev.isa.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class OrderDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        User user = TestUtil.user();
        user.setId(1L);
        Car car = TestUtil.car();
        car.setOwner(user);
        Order order = TestUtil.order();
        order.setCar(car);
        order.setOperationList(TestUtil.operations());
        order.setPointsSpend(100);
        order.setCost(100.);
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        order.setState(State.ACCEPTED);
        User userOne = TestUtil.user();
        userOne.setId(1L);
        Car carOne = TestUtil.car();
        carOne.setOwner(user);
        Order orderOne = TestUtil.order();
        orderOne.setCar(car);
        orderOne.setOperationList(TestUtil.operations());
        orderOne.setPointsSpend(100);
        orderOne.setCost(100.);
        orderOne.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        orderOne.setState(State.ACCEPTED);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(orderOne);
        OrderDto orderDto = OrderDto.from(order);
        OrderDto orderDtoOne = OrderDto.fromRebuildOrder(order);
        Order orderTwo = OrderDto.newOrder(orderDto);
        List<OrderDto> orderDtos = OrderDto.from(orders);
        Order orderThree = OrderDto.rebuildOrder(orderDtoOne,TestUtil.car(),"test",TestUtil.operations());


        assertNotNull(orderDto);
        assertNotNull(orderDtoOne);
        assertNotNull(orderDtos);
        assertNotNull(orderThree);
    }
}

