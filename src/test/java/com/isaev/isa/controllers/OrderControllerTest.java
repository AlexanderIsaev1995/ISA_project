package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.dto.AddOrderDto;
import com.isaev.isa.dto.UpdateOrderDto;
import com.isaev.isa.entities.Car;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.User;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.services.CarService;
import com.isaev.isa.services.OrderService;
import com.isaev.isa.services.UserService;
import com.isaev.isa.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = IsaApplication.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CarService carService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConversionService conversionService;

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void createOrderTest() throws Exception{
        User user = new User();
        AddOrderDto addOrderDto = new AddOrderDto();
        addOrderDto.setCarBrand("test");
        addOrderDto.setCarModel("test");
        addOrderDto.setCarNumber("test");
        addOrderDto.setDescription("test");
        Order order = addOrderDto.toCreateOrder();
        Car car = conversionService.convert(addOrderDto,Car.class);
        Order expectedOrder = new Order();
        when(orderService.add(order,car)).thenReturn(expectedOrder);
        when(userService.getUser()).thenReturn(user);
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(addOrderDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void getByIdOrderExistTest() throws Exception{
        Order order = TestUtil.order();
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        when(orderService.readOne(1L)).thenReturn(order);
        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void getByIdOrderNotExistTest() throws Exception{
        when(orderService.readOne(1L)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.orderDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByIdAccessDeniedTest() throws Exception{
        when(orderService.readOne(1L)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.orderDto())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void updateByUserAllIsOkTest() throws Exception{
        User user = new User();
        UpdateOrderDto updateOrderDto = new UpdateOrderDto();
        updateOrderDto.setCarBrand("test");
        updateOrderDto.setCarModel("test");
        updateOrderDto.setCarNumber("test");
        updateOrderDto.setDescription("test");
        updateOrderDto.setPointsSpend(100);
        Car car = new Car();
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");
        Order newOrder =  updateOrderDto.newOrder();
        newOrder.setId(1L);
        newOrder.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        newOrder.setCar(car);
        when(orderService.updateByUser(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(newOrder);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(TestUtil.order()));
        mockMvc.perform(patch("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateOrderDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void updateByUserOrderHaveNotAccessStatementTest() throws Exception{
        when(orderService.updateByUser(TestUtil.order(),TestUtil.car(),1L)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(patch("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.orderDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void UpdateByUserAccessDeniedTest() throws Exception{
        when(orderService.updateByUser(TestUtil.order(),TestUtil.car(),1L)).thenReturn(TestUtil.order());
        mockMvc.perform(patch("/orders/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.orderDto())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "AddOneOrder")
    public void getAllTest() throws Exception{
        Car car = new Car();
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");
        Order order = TestUtil.order();
        order.setCar(car);
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));

        when(orderService.readAllByCarOwnerId()).thenReturn(Arrays.asList(order));
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrders())))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAccessDeniedTest() throws Exception{
        when(orderService.updateByUser(TestUtil.order(),TestUtil.car(),1L)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.orderDto())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "ReadAllOrders")
    public void readAllOrdersExistsTest() throws Exception{
        User user = TestUtil.user();
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");
        Order order = TestUtil.order();
        order.setCar(car);
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));

        when(orderService.readAll()).thenReturn(Arrays.asList(order));
        mockMvc.perform(get("/orders/service")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrdersService())))
                .andExpect(status().isOk());
    }

    @Test
    public void readAllAccessDeniedTest() throws Exception{
        when(orderService.readAll()).thenReturn(TestUtil.allOrders());
        mockMvc.perform(get("/orders/service")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrdersService())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "ReadAllOrders")
    public void readAllStateWhenStateIsValidTest() throws Exception{
        User user = TestUtil.user();
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");
        Order order = TestUtil.order();
        order.setCar(car);
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));

        when(orderService.readAllState("accepted")).thenReturn(Arrays.asList(order));
        mockMvc.perform(get("/orders/service/accepted")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrdersService())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ReadAllOrders")
    public void readAllStateWhenStateIsNotValidTest() throws Exception{
        when(orderService.readAllState("test")).thenReturn(TestUtil.allOrders());
        mockMvc.perform(get("/orders/test")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrdersService())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void readAllStateAccessDeniedTest() throws Exception{
        when(orderService.readAllState("accepted")).thenReturn(TestUtil.allOrders());
        mockMvc.perform(get("/orders/service/accepted")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.allOrdersService())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(authorities = "ReadAllOrders")
    public void updateStateTest() throws Exception{
        User user = TestUtil.user();
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("test");
        car.setModel("test");
        car.setNumber("test");
        Order order = TestUtil.order();
        order.setCar(car);
        order.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
        when(orderService.updateState(Mockito.any(),Mockito.any())).thenReturn(order);
        mockMvc.perform(patch("/orders/service/states/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString("accepted")))
                .andExpect(status().isOk());
    }

    @Test
    public void updateStateAccessDeniedTest() throws Exception{
        when(orderService.updateState(1L,"accepted")).thenReturn(TestUtil.order());
        mockMvc.perform(patch("/orders/service/states/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.order())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateOperationsAccessDeniedTest() throws Exception{
        when(orderService.updateOperations(1L,TestUtil.operations())).thenReturn(TestUtil.order());
        mockMvc.perform(patch("/orders/service/operations/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.order())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateCostAccessDeniedTest() throws Exception{
        when(orderService.updateCost(1L,10000.)).thenReturn(TestUtil.order());
        mockMvc.perform(patch("/orders/service/costs/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(TestUtil.order())))
                .andExpect(status().is4xxClientError());
    }
}
