package com.isaev.isa.dto;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.enums.DiscountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDto {

    String discountStatus;

    public Discount toDiscount() {
        return new Discount(DiscountStatus.valueOf(discountStatus.toUpperCase()));
    }

    public static DiscountDto fromDiscount(Discount discount) {
        return DiscountDto.builder()
                .discountStatus(discount.getDiscountStatus().name())
                .build();
    }
}
