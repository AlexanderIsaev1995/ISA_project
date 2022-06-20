package com.isaev.isa.services.impl;

import com.isaev.isa.entities.*;
import com.isaev.isa.entities.enums.State;
import com.isaev.isa.repositories.CarRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.security.JwtTokenProvider;
import com.isaev.isa.services.CarService;
import com.isaev.isa.services.DiscountService;
import com.isaev.isa.services.OperationService;
import com.isaev.isa.services.UserService;
import com.isaev.isa.services.impl.OrderServiceImpl;
import com.isaev.isa.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private DiscountService discountService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarService carService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OperationService operationService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void addTestAllIsOk(){
        Order inputOrder = TestUtil.order();
        inputOrder.setDescription("test");
        inputOrder.setPointsSpend(10);
        Car inputCar = new Car();
        User receivedUser = TestUtil.user();
        Discount receivedDiscount = TestUtil.discount();
        Car car = TestUtil.car();
        Order expectedResult = Order.acceptedOrderForAdd(car,inputOrder.getDescription(), inputOrder.getPointsSpend());
        expectedResult.setCreationTime(null);


        Mockito.when(userService.getUser()).thenReturn(receivedUser);
        Mockito.when(discountService.checkDiscount(receivedUser)).thenReturn(receivedDiscount);
        Mockito.when(carService.carExists(inputCar)).thenReturn(car);
        Order resultTest = orderService.add(inputOrder,inputCar);
        resultTest.setCreationTime(null);

        Assert.assertEquals(expectedResult,resultTest);
        Assert.assertNotNull(resultTest);
        Mockito.verify(userService, Mockito.times(1)).getUser();
        Mockito.verify(discountService, Mockito.times(1)).checkDiscount(receivedUser);
        Mockito.verify(carService, Mockito.times(1)).carExists(inputCar);
    }

    @Test
    public void updateByUserTest(){
        LocalDateTime creationTime = LocalDateTime.now();

        Order oldOrder = TestUtil.order();
        oldOrder.setId(1L);

        Order newOrder = TestUtil.order();

        Car car = new Car();
        Car newCar = TestUtil.car();

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCar(TestUtil.car());
        updatedOrder.setState(State.ACCEPTED);
        updatedOrder.setDescription("test");

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(oldOrder));
        if(!oldOrder.getState().equals(State.ACCEPTED)){
            throw new IllegalArgumentException("The order is already in progress");
        }
        Mockito.when(carService.carExists(car)).thenReturn(newCar);
        Order result = orderService.updateByUser(newOrder,car,1L);
        result.setCreationTime(null);

        Assert.assertEquals(updatedOrder,result);
        Assert.assertNotNull(result);
        Mockito.verify(orderRepository, Mockito.times(2)).findById(1L);
        Mockito.verify(carService, Mockito.times(1)).carExists(car);
    }

    @Test
    public void updateByUserFailTest(){
        Order order = TestUtil.order();
        try{
            if(!order.getState().equals(State.ACCEPTED)){
                throw new IllegalArgumentException("The order is already in progress");
            }
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateOperationTest(){
        Order order = TestUtil.order();
        Car car = new Car();
        User owner = new User();
        car.setOwner(owner);
        order.setCar(car);
        Set<Operation> operationSet = Stream.of(new Operation()).collect(Collectors.toSet());
        Order expectedResult = new Order();
        expectedResult.setOperationList(operationSet);
        expectedResult.setState(State.ACCEPTED);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(expectedResult));
        Mockito.when(operationService.checkOperation(Mockito.any())).thenReturn(new Operation());
        Order result = orderService.updateOperations(1L,operationSet);
        result.setCreationTime(null);

        Assert.assertEquals(expectedResult,result);
        Assert.assertNotNull(result);
        Mockito.verify(orderRepository, Mockito.times(2)).findById(1L);
    }

    @Test
    public void updateStateTest(){
        String state = "COMPLETED";
        Order inputOrder = new Order();
        inputOrder.setId(1L);
        Order expected = new Order();
        expected.setId(1L);
        expected.setState(State.COMPLETED);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(expected));
        Order result = orderService.updateState(1L,state);
        Assert.assertSame(expected,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void updateStateWrongStateTest(){
        String state = "test";
        Order inputOrder = new Order();
        inputOrder.setId(1L);

        try{
            Order result = orderService.updateState(1L,state);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    public void updateCostAllIsOkTest(){
        Discount discount =TestUtil.discount();
        discount.setId(1L);
        User returnedUser = new User();
        returnedUser.setId(1L);
        returnedUser.setDiscount(discount);
        returnedUser.setBonusPoints(100.);
        Car car = TestUtil.car();
        car.setId(1L);
        car.setOwner(returnedUser);

        Order firstReturnedOrder = TestUtil.order();
        firstReturnedOrder.setId(1L);
        firstReturnedOrder.setCar(car);
        firstReturnedOrder.setPointsSpend(100);
        Double cost = 10000.;

        Order secondReturnedOrder = TestUtil.order();
        secondReturnedOrder.setId(1L);
        secondReturnedOrder.setCar(car);
        secondReturnedOrder.setPointsSpend(100);
        secondReturnedOrder.setCost(9900.);

        Mockito.when(userService.getUser()).thenReturn(returnedUser);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(firstReturnedOrder));
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(secondReturnedOrder));
        Order result = orderService.updateCost(1L,cost);
        Mockito.verify(userService,Mockito.times(1)).getUser();
        Mockito.verify(orderRepository,Mockito.times(2)).findById(1L);
        Assert.assertEquals(secondReturnedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void updateCostWithIfStatementIsFalseTest(){
        Discount discount =TestUtil.discount();
        discount.setId(1L);
        User returnedUser = new User();
        returnedUser.setId(1L);
        returnedUser.setDiscount(discount);
        returnedUser.setBonusPoints(100.);
        Car car = TestUtil.car();
        car.setId(1L);
        car.setOwner(returnedUser);

        Order firstReturnedOrder = TestUtil.order();
        firstReturnedOrder.setId(1L);
        firstReturnedOrder.setCar(car);
        firstReturnedOrder.setPointsSpend(200);
        Double cost = 10000.;

        Order secondReturnedOrder = TestUtil.order();
        secondReturnedOrder.setId(1L);
        secondReturnedOrder.setCar(car);
        secondReturnedOrder.setPointsSpend(100);
        secondReturnedOrder.setCost(cost);

        Mockito.when(userService.getUser()).thenReturn(returnedUser);
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(firstReturnedOrder));
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(secondReturnedOrder));
        Order result = orderService.updateCost(1L,cost);
        Mockito.verify(userService,Mockito.times(1)).getUser();
        Mockito.verify(orderRepository,Mockito.times(2)).findById(1L);
        Assert.assertEquals(secondReturnedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void readOneWhenOrderRepositoryFindOrderTest(){
        Order returnedOrder = TestUtil.order();
        returnedOrder.setId(1L);

        Order expectedOrder = new Order();
        Car car = TestUtil.car();
        expectedOrder.setCar(car);
        expectedOrder.setState(State.ACCEPTED);
        expectedOrder.setId(1L);
        expectedOrder.setDescription("test");

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(returnedOrder));
        Order result = orderService.readOne(1L);
        result.setCreationTime(null);

        Assert.assertNotNull(result);
        Assert.assertEquals(expectedOrder,result);
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readOneWhenOrderRepositoryNotFindOrderTest(){
        Mockito.when(orderRepository.findById(1L)).thenThrow(IllegalArgumentException.class);
        try{
            Order result = orderService.readOne(1L);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readOneServiceWhenOrderRepositoryFindOrderTest(){
        Order expectedOrder = new Order();
        expectedOrder.setCar(TestUtil.car());
        expectedOrder.setState(State.ACCEPTED);
        expectedOrder.setId(1L);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(expectedOrder));
        Order result = orderService.readOneService(1L);
        result.setCreationTime(null);

        Assert.assertNotNull(result);
        Assert.assertEquals(expectedOrder,result);
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readOneServiceWhenOrderRepositoryNotFindOrderTest(){
        Mockito.when(orderRepository.findById(1L)).thenThrow(IllegalArgumentException.class);
        try{
            Order result = orderService.readOneService(1L);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        Mockito.verify(orderRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    public void readAllWhenOrderRepositoryFindOrdersTest(){
        User user = new User();
        user.setId(1L);
        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Order order = new Order();
        order.setId(1L);
        order.setCar(car);
        order.setCreationTime(now);
        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setCar(car);
        orderTwo.setCreationTime(now);
        List<Order> orders = new ArrayList<>();
        orders.addAll(Arrays.asList(order,orderTwo));

        List<Order> expectedOrders = orders;

        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.readAll();

        Assert.assertEquals(expectedOrders,result);
        Assert.assertNotNull(result);
        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void readAllWhenOrderRepositoryNotFindOrdersTest(){
        Mockito.when(orderRepository.findAll()).thenThrow(IllegalArgumentException.class);
        try{
            List<Order> result = orderService.readAll();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        Mockito.verify(orderRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void readAllByCarOwnerId(){
        User user = new User();
        user.setId(1L);
        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Order order = new Order();
        order.setId(1L);
        order.setCar(car);
        order.setCreationTime(now);
        Order orderTwo = new Order();
        orderTwo.setId(2L);
        orderTwo.setCar(car);
        orderTwo.setCreationTime(now);
        List<Order> orders = new ArrayList<>();
        orders.addAll(Arrays.asList(order,orderTwo));

        List<Order> expectedOrders = orders;

        Mockito.when(userService.getUser()).thenReturn(user);
        Mockito.when(orderRepository.findAllByCarOwnerId(1L)).thenReturn(orders);
        List<Order> result = orderService.readAllByCarOwnerId();

        Assert.assertEquals(expectedOrders,result);
        Assert.assertNotNull(result);
        Mockito.verify(orderRepository,Mockito.times(1)).findAllByCarOwnerId(1L);
    }

    @Test
    public void readAllByCarOwnerIdWhenUserRepositoryNotFoundUserTest(){
        Mockito.when(userService.getUser()).thenThrow(UsernameNotFoundException.class);
        try {
            orderService.readAllByCarOwnerId();
        } catch (UsernameNotFoundException e){
            e.printStackTrace();
        }
        Mockito.verify(userService,Mockito.times(1)).getUser();
    }

    @Test
    public void readAllState(){
        User user = new User();
        user.setId(1L);
        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Order order = new Order();
        order.setId(2L);
        order.setCar(car);
        order.setState(State.COMPLETED);
        order.setCreationTime(now);

        List<Order> orders = new ArrayList<>();
        orders.addAll(Arrays.asList(order));

        List<Order> expectedOrders = Arrays.asList(order);
        String state = "COMPLETED";
        Mockito.when(orderRepository.findAllByState(State.COMPLETED)).thenReturn(orders);
        List<Order> result = orderService.readAllState(state);

        Assert.assertEquals(expectedOrders,result);
        Assert.assertNotNull(result);
        Mockito.verify(orderRepository,Mockito.times(1)).findAllByState(State.COMPLETED);
    }

    @Test
    public void readAllStateWrongStateTest(){
        String state = "test";
        try{
            orderService.readAllState(state);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    public void rebuildOrderWhenAllIfStatementIsTrueTest(){
        Order oldOrder = new Order();
        Order newOrder = TestUtil.order();
        newOrder.setPointsSpend(100);
        User user = new User();
        user.setId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");

        Order expectedOrder = new Order();
        expectedOrder.setCar(car);
        expectedOrder.setDescription("test");
        expectedOrder.setPointsSpend(100);

        Order result = orderService.rebuildOrder(newOrder,oldOrder,car);
        result.setCreationTime(null);

        Assert.assertEquals(expectedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void rebuildOrderWhenAllIfStatementIsFalseTest(){
        Order newOrder = new Order();
        Order oldOrder = TestUtil.order();
        oldOrder.setPointsSpend(100);
        User user = new User();
        user.setId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");

        Order expectedOrder = new Order();
        expectedOrder.setCar(car);
        expectedOrder.setDescription("test");
        expectedOrder.setState(State.ACCEPTED);
        expectedOrder.setPointsSpend(100);

        Order result = orderService.rebuildOrder(newOrder,oldOrder,car);
        result.setCreationTime(null);

        Assert.assertEquals(expectedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void rebuildOrderWhenAllFirstIfStatementIsTrueSecondIfStatementIsFalseTest(){
        Order oldOrder = new Order();
        oldOrder.setPointsSpend(100);
        Order newOrder = TestUtil.order();
        User user = new User();
        user.setId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");

        Order expectedOrder = new Order();
        expectedOrder.setCar(car);
        expectedOrder.setDescription("test");
        expectedOrder.setPointsSpend(100);

        Order result = orderService.rebuildOrder(newOrder,oldOrder,car);
        result.setCreationTime(null);

        Assert.assertEquals(expectedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void rebuildOrderWhenAllFirstIfStatementIsFalseSecondIfStatementIsTrueTest(){
        Order oldOrder = new Order();
        oldOrder.setDescription("test");
        Order newOrder = TestUtil.order();
        newOrder.setPointsSpend(100);
        newOrder.setDescription(null);
        User user = new User();
        user.setId(1L);

        Car car = new Car();
        car.setId(1L);
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");

        Order expectedOrder = new Order();
        expectedOrder.setCar(car);
        expectedOrder.setDescription("test");
        expectedOrder.setPointsSpend(100);

        Order result = orderService.rebuildOrder(newOrder,oldOrder,car);
        result.setCreationTime(null);

        Assert.assertEquals(expectedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void getStateWrongStateTest(){
        try{
            State result = orderService.getState("test");
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getStateInWorkTest(){
        State expectedState = State.IN_WORK;
        State result = orderService.getState("in_work");

        Assert.assertEquals(expectedState,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void getStateCompletedTest(){
        State expectedState = State.COMPLETED;
        State result = orderService.getState("completed");

        Assert.assertEquals(expectedState,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void getOrderTest(){
        Order returnedOrder = new Order();
        Order expectedOrder = new Order();

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(returnedOrder));
        Order result = orderService.getOrder(1L);

        Assert.assertEquals(expectedOrder,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void getOrderFailTest(){
        Mockito.when(orderRepository.findById(1L)).thenThrow(IllegalArgumentException.class);
        try{
            Order order = orderService.getOrder(1L);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
