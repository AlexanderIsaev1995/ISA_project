package com.isaev.isa.entities;

import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.entities.enums.Status;
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
@Table(name = "users")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "user_id"))})
public class User extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "bonus_points")
    private Double bonusPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;

    @ManyToOne
    @JoinColumn(name = "discount")
    private Discount discount;

    public static User fromReg(String username, String password, String firstName, String lastName) {
        return User.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
    }

    public static User toWorker(String username, String password, String firstName, String lastName) {
        return User.builder()
                .username(username)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.WORKER)
                .status(Status.ACTIVE)
                .build();
    }

    public static User toAdmin(String username, String password) {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.ADMIN)
                .status(Status.ACTIVE)
                .build();
    }
}

