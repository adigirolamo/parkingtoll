package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum ParkingSlotState implements BaseEnum {

    FREE("FREE"), RESERVED("RESERVED"), PAYING("PAYING"), PAYED("PAYED");

    private String code;

    ParkingSlotState(String code) {
        this.code = code;
    }
}
