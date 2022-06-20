package com.isaev.isa.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {

    ENGINE_FULL("Capital engine repair"),
    ENGINE_LITE("Scheduled engine maintenance"),
    MAINTENANCE("Replacement of filters, brake pads and technical fluids"),
    GEARBOX_FULL("Capital gearbox repair"),
    GEARBOX_LITE("Scheduled gearbox maintenance"),
    BRAKE_SYSTEM("Brake system service"),
    WHEEL("Wheel alignment"),
    HEADLIGHT("Headlight adjustment"),
    SUSPENSION("Suspension repair");

    private final String operationDescription;


}
