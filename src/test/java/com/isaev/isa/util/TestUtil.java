package com.isaev.isa.util;

import com.isaev.isa.dto.OrderDto;
import com.isaev.isa.dto.OrderDtoService;
import com.isaev.isa.dto.TokenDto;
import com.isaev.isa.dto.UserDto;
import com.isaev.isa.entities.*;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.entities.enums.OperationType;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.entities.enums.State;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtil {

    public static User worker(){
        return User.builder()
                .username("worker")
                .password("worker")
                .firstName("worker")
                .lastName("worker")
                .role(Role.WORKER)
                .build();
    }

    public static User admin(){
        return User.builder()
                .username("admin")
                .password("admin")
                .firstName("admin")
                .lastName("admin")
                .role(Role.ADMIN)
                .build();
    }

    public static User user() {
        return User.builder()
                .username("user")
                .password("user")
                .firstName("user")
                .lastName("user")
                .role(Role.USER)
                .build();
    }


    public static UserDto authDto(){
        return UserDto.builder()
                .username("user")
                .password("user")
                .build();
    }

    public static UserDto regUserDto(){
        return UserDto.builder()
                .username("user")
                .password("user")
                .firstName("user")
                .lastName("user")
                .role(Role.USER)
                .build();
    }

    public static UserDto addWorkerDto(){
        return UserDto.builder()
                .username("worker")
                .password("worker")
                .firstName("worker")
                .lastName("worker")
                .role(Role.WORKER)
                .build();
    }

    public static UserDto addAdminDto(){
        return UserDto.builder()
                .username("admin")
                .password("admin")
                .firstName("admin")
                .lastName("admin")
                .role(Role.ADMIN)
                .build();
    }
    public static Car car(){
        return Car.builder()
                .brand("Renault")
                .model("Sandero")
                .number("M298EX152")
                .build();
    }

    public static Order orderAccepted(){
        return Order.builder()
                .description("test")
                .creationTime(Timestamp.valueOf(LocalDateTime.now()))
                .state(State.ACCEPTED)
                .build();
    }

    public static Order orderInWork(){
        return Order.builder()
                .description("test")
                .creationTime(Timestamp.valueOf(LocalDateTime.now()))
                .state(State.IN_WORK)
                .build();
    }

    public static Order orderCompleted(){
        return Order.builder()
                .description("test")
                .state(State.COMPLETED)
                .build();
    }


    public static ResponseEntity<OrderDto> addOrderDto(){
        return ResponseEntity.ok(OrderDto.builder()
                .id(1L)
                .carBrand("test")
                .carModel("test")
                .carNumber("test")
                .state(State.ACCEPTED)
                .description("test")
                .build());
    }

    public static OrderDto orderDto(){
        return OrderDto.builder()
                .id(1L)
                .carBrand("test")
                .carModel("test")
                .carNumber("test")
                .state(State.ACCEPTED)
                .description("test")
                .build();
    }

    public static OrderDtoService orderDtoService(){
        return OrderDtoService.builder()
                .id(1L)
                .carBrand("test")
                .carModel("test")
                .carNumber("test")
                .state(State.ACCEPTED)
                .description("test")
                .build();
    }

    public static Order order(){
        return Order.builder()
                .car(car())
                .state(State.ACCEPTED)
                .description("test")
                .build();
    }

    public static OrderDto updateOrderDto(){
        return OrderDto.builder()
                .id(1L)
                .carBrand("test")
                .carModel("test")
                .carNumber("test")
                .state(State.COMPLETED)
                .description("test")
                .build();
    }

    public static List<Order> allOrders() {
        return Arrays.asList(Order.builder()
                .state(State.ACCEPTED)
                .description("test")
                .build());
    }
    public static List<OrderDtoService> allOrdersService() {
        return Arrays.asList(OrderDtoService.builder()
                .id(1L)
                .carBrand("test")
                .carModel("test")
                .carNumber("test")
                .state(State.ACCEPTED)
                .description("test")
                .build());
    }

    public static Set<Operation> operations(){
        return Stream.of(new Operation(OperationType.ENGINE_FULL,"engine",10000)).collect(Collectors.toSet());
    }

    public static Discount discount(){
        return new Discount(DiscountStatus.NO_DISCOUNT);
    }



    public static TokenDto tokenDto(){
        return TokenDto.from("token","user","USER");
    }

    public static TokenDto tokenWorkerDto(){
        return TokenDto.from("token","worker","WORKER");
    }

    public static TokenDto tokenAdminDto(){
        return TokenDto.from("token","admin","ADMIN");
    }
}
