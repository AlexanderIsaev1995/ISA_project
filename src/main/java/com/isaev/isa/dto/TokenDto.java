package com.isaev.isa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    private String value;
    private String username;
    private String role;

    public static TokenDto from(String value, String username, String role) {
        return TokenDto.builder()
                .value(value)
                .username(username)
                .role(role)
                .build();
    }
}
