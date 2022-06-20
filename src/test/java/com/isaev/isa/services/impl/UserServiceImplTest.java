package com.isaev.isa.services.impl;

import com.isaev.isa.dto.TokenDto;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.exсeptions.UserAlreadyExistException;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.security.JwtTokenProvider;
import com.isaev.isa.services.DiscountService;
import com.isaev.isa.services.impl.UserServiceImpl;
import com.isaev.isa.util.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private DiscountService discountService;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void addUserRepositoryNotExistTest() {
        User inputUser = new User();

        User savedUser = new User();
        savedUser.setRole(Role.USER);

        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(false);
        Mockito.when(discountService.newDiscount(Role.USER)).thenReturn(new Discount());
        Mockito.when(userRepository.save(inputUser)).thenReturn(savedUser);

        User result = userService.registration(inputUser);
        Assert.assertSame(savedUser, result);
        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(inputUser.getUsername());
        Mockito.verify(discountService, Mockito.times(1)).newDiscount(Role.USER);
    }

    @Test
    public void addUserRepositoryExistTest() {
        User inputUser = new User();
        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(true);
        try{
            userService.registration(inputUser);
        } catch (UserAlreadyExistException e){
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(inputUser.getUsername());
    }

    @Test
    public void loginTest(){
        User inputUser = TestUtil.user();
        String valueToken = "token";
        Optional<User> returnedUser = Optional.of(inputUser);
        TokenDto token = TokenDto.from(valueToken,returnedUser.get().getUsername(),returnedUser.get().getRole().name());
        Mockito.when(userRepository.findByUsername(inputUser.getUsername())).thenReturn(returnedUser);
        Mockito.when(jwtTokenProvider.createToken(returnedUser.get().getUsername(),returnedUser.get().getRole().name())).thenReturn(valueToken);
        TokenDto result = userService.login(inputUser);
        Assert.assertNotNull(token);
        Assert.assertEquals(token,result);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(inputUser.getUsername());
        Mockito.verify(jwtTokenProvider, Mockito.times(1)).createToken(returnedUser.get().getUsername(),returnedUser.get().getRole().name());
    }

    @Test
    public void loginFailedTest(){
        User inputUser = TestUtil.user();
        try{
            User candidate = userRepository.findByUsername(inputUser.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User doesn't exist"));
        } catch (UsernameNotFoundException e){
            e.printStackTrace();
        }
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(inputUser.getUsername());
    }

    @Test
    public void registrationTest() {
        User inputUser = new User();
        User expectedUser = TestUtil.user();

        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(inputUser)).thenReturn(expectedUser);
        Mockito.when(discountService.newDiscount(Mockito.any())).thenReturn(new Discount());
        User result = userService.registration(inputUser);
        Assert.assertEquals(expectedUser, result);
        Mockito.verify(userRepository, Mockito.times(1)).existsUserByUsername(inputUser.getUsername());
        Mockito.verify(discountService, Mockito.times(1)).newDiscount(Mockito.any());
    }

    @Test
    public void registrationFailTest(){
        User inputUser = new User();
        Mockito.when(userRepository.existsUserByUsername(inputUser.getUsername())).thenReturn(true);
        try {
            userService.registration(inputUser);
        }catch (UserAlreadyExistException e){
            e.printStackTrace();
        }
    }
}
