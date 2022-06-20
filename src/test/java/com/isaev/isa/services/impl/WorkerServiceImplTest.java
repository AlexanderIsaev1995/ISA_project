package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.security.JwtTokenProvider;
import com.isaev.isa.services.DiscountService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class WorkerServiceImplTest {

    @InjectMocks
    private WorkerServiceImpl workerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private DiscountService discountService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void addUserRepositoryNotExistTest() {
        User inputUser = new User();

        User savedUser = new User();
        savedUser.setRole(Role.WORKER);

        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(inputUser)).thenReturn(savedUser);
        Mockito.when(discountService.newDiscount(Role.USER)).thenReturn(new Discount());
        User result = workerService.registration(inputUser);
        Assert.assertSame(savedUser, result);
        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(inputUser.getUsername());
        Mockito.verify(discountService, Mockito.times(1)).newDiscount(Role.USER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserRepositoryExistTest() {
        User inputUser = new User();
        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(true);
        workerService.registration(inputUser);

        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(inputUser.getUsername());
    }
}
