package com.isaev.isa.services.impl;

import com.isaev.isa.entities.User;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.services.DiscountService;
import com.isaev.isa.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("AdminServiceImpl")
@AllArgsConstructor
@Slf4j
public class AdminServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DiscountService discountService;

    @Override
    public User registration(User u) {
        if (!userRepository.existsUserByUsername(u.getUsername())) {
            log.debug("Admin with username - {} added.", u.getUsername());
            u.setDiscount(discountService.newDiscount(u.getRole()));
            return userRepository.save(u);
        } else {
            throw new IllegalArgumentException("A admin with the same name already exists");
        }
    }
}
