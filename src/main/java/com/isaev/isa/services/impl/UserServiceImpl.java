package com.isaev.isa.services.impl;

import com.isaev.isa.dto.TokenDto;
import com.isaev.isa.entities.User;
import com.isaev.isa.exсeptions.UserAlreadyExistException;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.security.JwtTokenProvider;
import com.isaev.isa.services.DiscountService;
import com.isaev.isa.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserServiceImpl")
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final DiscountService discountService;

    @Override
    public User registration(User u) {
        if (!userRepository.existsUserByUsername(u.getUsername())) {
            log.debug("User with username - {} added.", u.getUsername());
            u.setDiscount(discountService.newDiscount(u.getRole()));
            return userRepository.save(u);
        } else throw new UserAlreadyExistException("A user with the same name already exists");
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public TokenDto login(User u) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));
        User candidate = userRepository.findByUsername(u.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        log.debug("A user with username - {} logged in and has role - {}",
                u.getUsername(), candidate.getRole().name());
        String tokenValue = jwtTokenProvider
                .createToken(candidate.getUsername(), candidate.getRole().name());
        return TokenDto.from(tokenValue, candidate.getUsername(), candidate.getRole().name());
    }

    public User getUser() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else username = principal.toString();
        log.debug("User with username - {} received.", username);
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
    }


}
