package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.controllers.LoginController;
import com.isaev.isa.dto.AuthDto;
import com.isaev.isa.dto.RegDto;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.services.DiscountService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = IsaApplication.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginController login;

    @MockBean
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @MockBean
    private DiscountService discountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginTest() throws Exception {
        AuthDto auth = new AuthDto();
        User input = auth.toUser();
        when(userService.login(input)).thenReturn(TestUtil.tokenDto());
        mockMvc.perform(post(login.LOGIN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(auth)))
                .andExpect(status().isOk());
    }

    @Test
    public void badCredential() throws Exception {
        when(userService.login(TestUtil.user())).thenThrow(SecurityException.class);
        mockMvc.perform(post(login.LOGIN_ERROR))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void registrationTest() throws Exception{
        RegDto regDto = new RegDto();
        regDto.setUsername("test");
        regDto.setPassword("test");
        regDto.setFirstName("test");
        regDto.setLastName("test");
        User user = User.fromReg("test","test","test","test");
        User returnedUser = TestUtil.user();
        returnedUser.setId(1L);
        when(userService.registration(Mockito.any())).thenReturn(returnedUser);
        Mockito.when(discountService.newDiscount(Role.USER)).thenReturn(new Discount());
        Mockito.when(discountService.newDiscount(Mockito.any())).thenReturn(new Discount());
        mockMvc.perform(post(login.REGISTRATION)
                .content(objectMapper.writeValueAsString(regDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void registrationFailTest() throws Exception{
        when(userService.registration(TestUtil.user())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post(login.REGISTRATION)
                .content(objectMapper.writeValueAsString(TestUtil.regUserDto()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

}


