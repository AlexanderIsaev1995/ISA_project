package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.dto.AuthDto;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import com.isaev.isa.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = IsaApplication.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("AdminServiceImpl")
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void registrationTest() throws Exception{
        AuthDto authDto = new AuthDto();
        authDto.setUsername("test");
        authDto.setPassword("test");
        User user = User.toAdmin("test","test");
        User returnedUser = TestUtil.user();
        returnedUser.setId(1L);
        when(userService.registration(user)).thenReturn(returnedUser);
        mockMvc.perform(post("/admins")
                .content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void registrationFailTest() throws Exception{
        AuthDto authDto = new AuthDto();
        User user = User.toAdmin("test","test");
        User returnedUser = TestUtil.user();
        returnedUser.setId(1L);
        when(userService.registration(user)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post("/admins")
                .content(objectMapper.writeValueAsString(authDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationAccessDeniedTest() throws Exception{
        when(userService.registration(TestUtil.user())).thenReturn(TestUtil.user());
        mockMvc.perform(post("/admins")
                .content(objectMapper.writeValueAsString(TestUtil.regUserDto()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }
}
