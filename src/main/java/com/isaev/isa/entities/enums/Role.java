package com.isaev.isa.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum Role {
    USER(Stream.of(Permission.READ_ONE, Permission.ADD_ONE)
            .collect(Collectors.toSet())),
    WORKER(Stream.of(Permission.READ_ONE, Permission.ADD_ONE, Permission.READ_ALL, Permission.CHANGE_ALL)
            .collect(Collectors.toSet())),
    ADMIN(Stream.of(Permission.READ_ONE, Permission.ADD_ONE, Permission.READ_ALL,
            Permission.CHANGE_ALL, Permission.ADD_DELETE_WORKER, Permission.ADD_DELETE_ADMIN)
            .collect(Collectors.toSet()));

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
