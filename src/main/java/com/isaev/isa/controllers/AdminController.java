package com.isaev.isa.controllers;

import com.isaev.isa.dto.AuthDto;
import com.isaev.isa.entities.User;
import com.isaev.isa.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@PreAuthorize("hasAuthority('AddDeleteAdmins')")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(@Qualifier("AdminServiceImpl") UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<User> addAdmin(@RequestBody AuthDto req) {
        User u = User.toAdmin(req.getUsername(), passwordEncoder.encode(req.getPassword()));
        log.debug("Request to add an admin with username - {} was sent", u.getUsername());
        return ResponseEntity.ok(userService.registration(u));
    }
}

