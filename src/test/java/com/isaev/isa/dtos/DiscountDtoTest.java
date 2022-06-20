package com.isaev.isa.dtos;

import com.isaev.isa.dto.DiscountDto;
import com.isaev.isa.entities.Discount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DiscountDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        DiscountDto discountDto = new DiscountDto();
        discountDto.setDiscountStatus("NO_DISCOUNT");
        Discount discount = discountDto.toDiscount();
        DiscountDto discountDtoNew = DiscountDto.fromDiscount(discount);

        assertNotNull(discount);
        assertNotNull(discountDtoNew);
    }
}

