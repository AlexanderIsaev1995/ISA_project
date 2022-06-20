package com.isaev.isa.dto;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Role role;
    private Discount discount;
    private Double bonusPoints;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .discount(user.getDiscount())
                .bonusPoints(user.getBonusPoints())
                .build();
    }


    public static List<UserDto> fromUser(List<User> users) {
        return users.stream().map(UserDto::fromUser).collect(Collectors.toList());
    }
}
