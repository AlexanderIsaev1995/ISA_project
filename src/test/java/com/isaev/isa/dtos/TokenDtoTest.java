package com.isaev.isa.dtos;

import com.isaev.isa.dto.TokenDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class TokenDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        TokenDto token = TokenDto.from("test", "test", "test");

        assertNotNull(token);
    }
}

