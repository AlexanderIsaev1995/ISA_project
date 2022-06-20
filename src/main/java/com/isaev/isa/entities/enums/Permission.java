package com.isaev.isa.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    READ_ONE("ReadOneOrder"),
    ADD_ONE("AddOneOrder"),
    READ_ALL("ReadAllOrders"),
    CHANGE_ALL("ChangeAllOrders"),
    ADD_DELETE_WORKER("AddDeleteWorkers"),
    ADD_DELETE_ADMIN("AddDeleteAdmins");

    private final String permission;


}
