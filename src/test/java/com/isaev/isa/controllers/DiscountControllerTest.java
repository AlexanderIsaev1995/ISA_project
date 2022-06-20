package com.isaev.isa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaev.isa.IsaApplication;
import com.isaev.isa.dto.DiscountDto;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.services.DiscountService;
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
public class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService discountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void addDiscountTest() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscountStatus("no_discount");
        Discount inputDiscount = discountDto.toDiscount();
        Discount expectedDiscount = new Discount();
        expectedDiscount.setDiscountStatus(DiscountStatus.NO_DISCOUNT);
        expectedDiscount.setCoefficientDiscount(1.);

        Mockito.when(discountService.addDiscount(inputDiscount)).thenReturn(expectedDiscount);
        mockMvc.perform(post("/discounts")
                .content(objectMapper.writeValueAsString(discountDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void addDiscountFailTest() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscountStatus("no_discount");
        Discount inputDiscount = discountDto.toDiscount();
        Discount expectedDiscount = new Discount();
        expectedDiscount.setDiscountStatus(DiscountStatus.NO_DISCOUNT);
        expectedDiscount.setCoefficientDiscount(1.);

        Mockito.when(discountService.addDiscount(inputDiscount)).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(post("/discounts")
                .content(objectMapper.writeValueAsString(discountDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void deleteDiscountTest() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscountStatus("no_discount");
        Discount inputDiscount = discountDto.toDiscount();
        String expectedString = new String();

        Mockito.when(discountService.deleteByDiscountStatus(inputDiscount.getDiscountStatus())).thenReturn(expectedString);
        mockMvc.perform(delete("/discounts")
                .content(objectMapper.writeValueAsString(discountDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "AddDeleteAdmins")
    public void deleteDiscountFailTest() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscountStatus("no_discount");
        Discount inputDiscount = discountDto.toDiscount();
        String expectedString = new String();

        Mockito.when(discountService.deleteByDiscountStatus(inputDiscount.getDiscountStatus())).thenThrow(IllegalArgumentException.class);
        mockMvc.perform(delete("/discounts")
                .content(objectMapper.writeValueAsString(discountDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }
}
