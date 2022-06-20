package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.dto.RegDto;
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
public class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier("WorkerServiceImpl")
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "AddDeleteWorkers")
    public void registrationTest() throws Exception{
        RegDto regDto = new RegDto();
        regDto.setUsername("test");
        regDto.setPassword("test");
        regDto.setFirstName("test");
        regDto.setLastName("test");
        User user = User.toWorker("test","test","test","test");
        User returnedUser = TestUtil.user();
        returnedUser.setId(1L);
        when(userService.registration(user)).thenReturn(returnedUser);
        mockMvc.perform(post("/workers")
                .content(objectMapper.writeValueAsString(regDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteWorkers")
    public void registrationFailTest() throws Exception{
        RegDto regDto = new RegDto();
        User user = User.toWorker("test","test","test","test");
        User returnedUser = TestUtil.user();
        returnedUser.setId(1L);
        when(userService.registration(user)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post("/workers")
                .content(objectMapper.writeValueAsString(regDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationAccessDeniedTest() throws Exception{
        when(userService.registration(TestUtil.user())).thenReturn(TestUtil.user());
        mockMvc.perform(post("/workers")
                .content(objectMapper.writeValueAsString(TestUtil.user()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }
}
