package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum Currency implements BaseEnum {

    EURO("EUR");

    private String code;

    private java.util.Currency currency;

    Currency(String code) {
        this.code = code;
    }
}
