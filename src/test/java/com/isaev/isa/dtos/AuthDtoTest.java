package com.isaev.isa.dtos;

import com.isaev.isa.dto.AuthDto;
import com.isaev.isa.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AuthDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        AuthDto dto = new AuthDto();
        User user = dto.toUser();

        assertNotNull(user);
    }
}

