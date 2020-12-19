package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum VehicleType implements BaseEnum {

    CAR("CAR");

    private String code;

    VehicleType(String code) {
        this.code = code;
    }
}
