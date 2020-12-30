package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum ParkingSlotState implements BaseEnum {

    FREE("FREE", "Parking slot"), RESERVED("RESERVED", "Parking slot"),
    PAYING("PAYING", "Bill"), PAID("PAID", "Bill");

    private String code;
    private String associatedEntity;

    ParkingSlotState(String code, String associatedEntity) {
        this.code = code;
        this.associatedEntity = associatedEntity;
    }
}
