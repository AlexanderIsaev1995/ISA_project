package com.isaev.isa.repository;

import com.isaev.isa.IsaApplication;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.State;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IsaApplication.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;

    private List<Order> testOrdersList = new ArrayList<>();
    private Car car;
    private User user;
    private Order testOrder;

    @Before
    public void before(){
        user = TestUtil.user();
        userRepository.save(user);
        car = TestUtil.car();
        User userFromDB = userRepository.findByUsername("user").get();
        car.setOwner(userFromDB);
        carRepository.save(car);
        testOrder = TestUtil.orderCompleted();
        Car carFromDB = carRepository.findByNumber("M298EX152").get();
        testOrder.setCar(carFromDB);
        testOrder.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        testOrdersList.addAll(Arrays.asList(TestUtil.orderAccepted(), TestUtil.orderAccepted(),
                TestUtil.orderInWork(), TestUtil.orderInWork()));
        orderRepository.save(testOrder);
        orderRepository.saveAll(testOrdersList);
    }

    @After
    public void clean(){
        orderRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findByIdTest(){
        Optional<Order> order = orderRepository.findById(1L);
        if(order.isPresent()){
            assertEquals(testOrder,order.get());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void findByIdFailTest(){
        Optional<Order> order = orderRepository.findById(10L);
        assertEquals(Optional.empty(),order);
    }

    @Test
    public void findAllByStateTest(){
        List<Order> ordersFromDB = orderRepository.findAllByState(State.ACCEPTED);
        List<Order> orders = testOrdersList.stream().filter(order -> order.getState().equals(State.ACCEPTED)).collect(Collectors.toList());
        assertEquals(ordersFromDB,orders);
    }

    @Test
    public void findAllByStateFailTest(){
        clean();
        List<Order> ordersFromDB = orderRepository.findAllByState(State.ACCEPTED);
        assertEquals(ordersFromDB,new ArrayList<>());
    }

    @Test
    public void findAllByCarOwnerIdTest(){
        List<Order> orders = Stream.of(testOrder).collect(Collectors.toList());
        List<Order> ordersFromDB = orderRepository.findAllByCarOwnerId(1L);
        ordersFromDB.get(0).setOperationList(null);

        assertEquals(orders,ordersFromDB);
        Assert.assertNotNull(orders);
        Assert.assertNotNull(ordersFromDB);
    }

    @Test
    public void findAllByCarOwnerIdFailTest(){
        List<Order> ordersFromDB = orderRepository.findAllByCarOwnerId(2L);
        assertEquals(ordersFromDB,new ArrayList<>());
    }

    @Test
    public void countAllByCarOwnerIdTest(){
        int numberOfOrders = 1;
        int numberFromDB = orderRepository.countAllByCarOwnerId(1L);
        assertEquals(numberOfOrders,numberFromDB);
        Assert.assertNotNull(numberFromDB);
    }

    @Test
    public void countAllByCarOwnerIdFailTest(){
        int numberFromDB = orderRepository.countAllByCarOwnerId(2L);
        assertEquals(0,numberFromDB);
    }

    @Test
    public void countAllByCarOwnerIdAndCreationTimeBetweenTest(){
        int numberOfOrders = 1;
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp nowMinusYear = Timestamp.valueOf(LocalDateTime.now().minusYears(1L));
        int numberFromDB = orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(1L,nowMinusYear,now);
        assertEquals(numberOfOrders,numberFromDB);
        Assert.assertNotNull(numberFromDB);
    }

    @Test
    public void countAllByCarOwnerIdAndCreationTimeBetweenFailTest(){
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp nowMinusYear = Timestamp.valueOf(LocalDateTime.now().minusYears(1L));
        int numberFromDB = orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(2L,nowMinusYear,now);
        assertEquals(0,numberFromDB);
    }

    @Test
    public void updateStateTest(){
        State state = State.IN_WORK;
        State oldOrderState = orderRepository.findById(1L).get().getState();
        orderRepository.updateState(state,1L);
        State newOrderState = orderRepository.findById(1L).get().getState();
        assertEquals(state,newOrderState);
        Assert.assertNotEquals(oldOrderState,newOrderState);
        Assert.assertEquals(State.IN_WORK,newOrderState);
        Assert.assertNotNull(oldOrderState);
        Assert.assertNotNull(newOrderState);
    }

    @Test
    public void updateCostTest(){
        Double cost = 50000.;
        Double oldCost = orderRepository.findById(1L).get().getCost();
        orderRepository.updateCost(50000.,1L);
        Double newCost = orderRepository.findById(1L).get().getCost();
        assertEquals(cost,newCost);
        Assert.assertNotNull(newCost);
    }
}
