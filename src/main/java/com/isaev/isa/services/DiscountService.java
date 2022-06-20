package com.isaev.isa.services;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.entities.enums.Role;

public interface DiscountService {

    Discount checkDiscount(User user);

    Discount newDiscount(Role role);

    Discount addDiscount(Discount discount);

    String deleteByDiscountStatus(DiscountStatus discountStatus);

    Discount findDiscount(DiscountStatus discountStatus);
}
