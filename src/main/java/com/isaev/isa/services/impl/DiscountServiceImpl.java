package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.exсeptions.DiscountAlreadyExistException;
import com.isaev.isa.exсeptions.DiscountNotFoundException;
import com.isaev.isa.repositories.DiscountRepository;
import com.isaev.isa.repositories.OrderRepository;
import com.isaev.isa.services.DiscountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class DiscountServiceImpl implements DiscountService {

    private final OrderRepository orderRepository;
    private final DiscountRepository discountRepository;

    @Override
    public Discount checkDiscount(User user) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp nowMinusYear = Timestamp.valueOf(LocalDateTime.now().minusYears(1));
        int ordersPerYear = orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(user.getId(), nowMinusYear, now);
        DiscountStatus discountStatus;

        if (ordersPerYear >= 12) {
            discountStatus = DiscountStatus.GOLD_DISCOUNT;
        } else if (ordersPerYear >= 6) {
            discountStatus = DiscountStatus.SILVER_DISCOUNT;
        } else discountStatus = DiscountStatus.NO_DISCOUNT;

        log.debug("User {} now have discount status {}", user.getUsername(), discountStatus.name());
        return discountRepository.findByDiscountStatus(discountStatus).orElseThrow(() -> new DiscountNotFoundException("Discount not found"));
    }

    @Override
    public Discount newDiscount(Role role) {
        if (role.equals(Role.WORKER)) {
            return findDiscount(DiscountStatus.EMPLOYEE_DISCOUNT);
        }
        return findDiscount(DiscountStatus.NO_DISCOUNT);
    }

    @Override
    public Discount addDiscount(Discount discount) {
        if (!discountRepository.existsDiscountByDiscountStatus(discount.getDiscountStatus())) {
            discountRepository.save(discount);
            return findDiscount(discount.getDiscountStatus());
        } else {
            throw  new DiscountAlreadyExistException("Discount already exists");
        }
    }

    @Override
    public String deleteByDiscountStatus(DiscountStatus discountStatus) {
        if (discountRepository.existsDiscountByDiscountStatus(discountStatus)) {
            discountRepository.deleteDiscountByDiscountStatus(discountStatus);
            return (String.format("Discount with discount status - {%s} has been removed", discountStatus.name()));
        } else {
            throw new DiscountNotFoundException(String.format("Discount with discount status - {%s} not found", discountStatus.name()));
        }
    }

    @Override
    public Discount findDiscount(DiscountStatus discountStatus){
        return discountRepository.findByDiscountStatus(discountStatus)
                .orElseThrow(() -> new DiscountNotFoundException("Discount not found"));
    }
}
