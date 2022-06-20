package com.isaev.isa.controllers;

import com.isaev.isa.dto.OperationDto;
import com.isaev.isa.entities.Operation;
import com.isaev.isa.services.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operations")
@PreAuthorize("hasAuthority('AddDeleteAdmins')")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class OperationController {

    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping
    public ResponseEntity<OperationDto> addOperation(@RequestBody OperationDto request) {
        Operation operation = request.toOperation();
        return ResponseEntity.ok(OperationDto.fromOperation(operationService.addOperation(operation)));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteOperation(@RequestBody OperationDto request) {
        Operation operation = request.toOperation();
        return ResponseEntity.ok(operationService.deleteByOperationType(operation.getOperationType()));
    }
}
