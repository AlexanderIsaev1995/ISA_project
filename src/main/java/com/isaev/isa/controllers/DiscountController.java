package com.isaev.isa.controllers;

import com.isaev.isa.dto.DiscountDto;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.services.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discounts")
@PreAuthorize("hasAuthority('AddDeleteAdmins')")
@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DiscountDto> addDiscount(@RequestBody DiscountDto request) {
        Discount discount = request.toDiscount();
        return ResponseEntity.ok(DiscountDto.fromDiscount(discountService.addDiscount(discount)));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDiscount(@RequestBody DiscountDto request) {
        Discount discount = request.toDiscount();
        return ResponseEntity.ok(discountService.deleteByDiscountStatus(discount.getDiscountStatus()));
    }

}
