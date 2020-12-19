package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum EngineType implements BaseEnum {

    GASOLINE("GASOLINE"), ELECTRIC_20KW("E20KW"), ELECTRIC_50KW("E50KW");

    private String code;

    EngineType(String code) {
        this.code = code;
    }
}
