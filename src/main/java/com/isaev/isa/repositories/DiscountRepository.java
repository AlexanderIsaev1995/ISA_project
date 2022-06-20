package com.isaev.isa.repositories;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.enums.DiscountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    boolean existsDiscountByDiscountStatus(DiscountStatus discountStatus);

    Optional<Discount> findByDiscountStatus(DiscountStatus discountStatus);

    void deleteDiscountByDiscountStatus(DiscountStatus discountStatus);
}
