package com.isaev.isa.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum State {
    ACCEPTED("accepted"),
    IN_WORK("in_work"),
    COMPLETED("in work");

    private final String permission;
}
