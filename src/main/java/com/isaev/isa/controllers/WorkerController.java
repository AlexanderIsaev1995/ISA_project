package com.isaev.isa.controllers;

import com.isaev.isa.dto.RegDto;
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
@RequestMapping("/workers")
@PreAuthorize("hasAuthority('AddDeleteWorkers')")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class WorkerController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WorkerController(@Qualifier("WorkerServiceImpl") UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<User> add(@RequestBody RegDto req) {
        User u = User.toWorker(req.getUsername(), passwordEncoder.encode(req.getPassword()), req.getFirstName(), req.getLastName());
        log.debug("Request to add an worker with username - {} was sent", u.getUsername());
        return ResponseEntity.ok(userService.registration(u));
    }

}
