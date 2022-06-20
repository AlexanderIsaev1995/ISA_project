package com.isaev.isa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
