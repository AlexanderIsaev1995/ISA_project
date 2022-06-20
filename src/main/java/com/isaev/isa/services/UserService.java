package com.isaev.isa.services;

import com.isaev.isa.dto.TokenDto;
import com.isaev.isa.entities.User;
import org.apache.commons.lang.NotImplementedException;

public interface UserService {

    default User getUser() {
        throw new NotImplementedException();
    }

    ;

    default void delete(Long id) {
        throw new NotImplementedException();
    }

    default TokenDto login(User u) {
        throw new NotImplementedException();
    }

    default User registration(User u) {
        throw new NotImplementedException();
    }

}
