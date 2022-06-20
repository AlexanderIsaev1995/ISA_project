package com.isaev.isa.entities;

import com.isaev.isa.entities.enums.DiscountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "discount_id"))})
@Table(name = "discount")
public class Discount extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "discount_status")
    private DiscountStatus discountStatus = DiscountStatus.NO_DISCOUNT;
    private double coefficientDiscount;

    public Discount(DiscountStatus discountStatus) {
        this.discountStatus = discountStatus;
        this.coefficientDiscount = calculateDiscount();
    }

    private double calculateDiscount() {
        switch (discountStatus) {
            case SILVER_DISCOUNT:
                return 0.95;
            case GOLD_DISCOUNT:
                return 0.9;
            case EMPLOYEE_DISCOUNT:
                return 0.8;
            case NO_DISCOUNT:
            default:
                return 1.;
        }
    }
}
