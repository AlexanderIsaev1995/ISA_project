package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.dto.DiscountDto;
import com.isaev.isa.dto.OperationDto;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.entities.enums.OperationType;
import com.isaev.isa.services.DiscountService;
import com.isaev.isa.services.OperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = IsaApplication.class)
public class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService operationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void addOperationTest() throws Exception {
        OperationDto operationDto = new OperationDto();
        operationDto.setOperationType("engine_full");
        operationDto.setDescription("test");
        operationDto.setCost(100);
        Operation inputOperation = operationDto.toOperation();
        Operation expectedOperation = new Operation();
        expectedOperation.setOperationType(OperationType.ENGINE_FULL);
        expectedOperation.setOperationDescription("test");
        expectedOperation.setCost(100);

        Mockito.when(operationService.addOperation(inputOperation)).thenReturn(expectedOperation);
        mockMvc.perform(post("/operations")
                .content(objectMapper.writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void addOperationFailTest() throws Exception {
        OperationDto operationDto = new OperationDto();
        operationDto.setOperationType("engine_full");
        operationDto.setDescription("test");
        operationDto.setCost(100);
        Operation inputOperation = operationDto.toOperation();
        Operation expectedOperation = new Operation();
        expectedOperation.setOperationType(OperationType.ENGINE_FULL);
        expectedOperation.setOperationDescription("test");
        expectedOperation.setCost(100);

        Mockito.when(operationService.addOperation(inputOperation)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post("/operations")
                .content(objectMapper.writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void deleteOperationTest() throws Exception {
        OperationDto operationDto = new OperationDto();
        operationDto.setOperationType("engine_full");
        operationDto.setDescription("test");
        operationDto.setCost(100);
        Operation inputOperation = operationDto.toOperation();
        String expectedString = new String();

        Mockito.when(operationService.deleteByOperationType(inputOperation.getOperationType())).thenReturn(expectedString);
        mockMvc.perform(delete("/operations")
                .content(objectMapper.writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void deleteOperationFailTest() throws Exception {
        OperationDto operationDto = new OperationDto();
        operationDto.setOperationType("engine_full");
        operationDto.setDescription("test");
        operationDto.setCost(100);
        Operation inputOperation = operationDto.toOperation();
        String expectedString = new String();

        Mockito.when(operationService.deleteByOperationType(inputOperation.getOperationType())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(delete("/discounts")
                .content(objectMapper.writeValueAsString(operationDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
