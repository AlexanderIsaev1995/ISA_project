package com.isaev.isa.controllers;

import com.isaev.isa.dto.AuthDto;
import com.isaev.isa.dto.RegDto;
import com.isaev.isa.dto.TokenDto;
import com.isaev.isa.dto.UserDto;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class LoginController {

    public static final String LOGIN = "/login";
    public static final String LOGIN_ERROR = "/login?error";
    public static final String LOGOUT = "/logout";
    public static final String REGISTRATION = "/registration";
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(@Qualifier("UserServiceImpl") UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(LOGIN)
    public ResponseEntity<TokenDto> login(@RequestBody AuthDto request) {
        User u = request.toUser();
        log.debug("Request to login user with username - {} was sent", u.getUsername());
        return ResponseEntity.ok(userService.login(u));
    }

    @PostMapping(LOGOUT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Request to logout was sent");
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
    }

    @PostMapping(REGISTRATION)
    public ResponseEntity<UserDto> register(@RequestBody RegDto req) {
        User u = User.fromReg(req.getUsername(), passwordEncoder.encode(req.getPassword()), req.getFirstName(), req.getLastName());
        log.debug("Request to registration user with username - {} was sent", u.getUsername());
        return ResponseEntity.ok(UserDto.fromUser(userService.registration(u)));
    }

}
