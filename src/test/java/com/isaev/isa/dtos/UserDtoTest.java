package com.isaev.isa.dtos;

import com.isaev.isa.dto.UserDto;
import com.isaev.isa.entities.User;
import com.isaev.isa.util.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserDtoTest {

    @Test
    public void AddOrderDtoSetUpTest() throws Exception {
        User user = TestUtil.user();
        User userNew = TestUtil.user();
        UserDto userDto = UserDto.fromUser(user);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(userNew);
        List<UserDto> userDtos = UserDto.fromUser(users);


        assertNotNull(userDto);
        assertNotNull(userDtos);
    }
}

