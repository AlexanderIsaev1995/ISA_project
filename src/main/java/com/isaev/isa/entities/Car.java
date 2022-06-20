package com.isaev.isa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "car")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "car_id"))})
public class Car extends BaseEntity {

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "number")
    private String number;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;


}
