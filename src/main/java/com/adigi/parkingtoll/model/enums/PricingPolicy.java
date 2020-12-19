package com.adigi.parkingtoll.model.enums;

import lombok.Getter;

@Getter
public enum PricingPolicy implements BaseEnum {
    ONLY_HOURS("HOUR"), INITIAL_FEE_PLUS_HOURS("FEE_HOUR");

    private String code;

    PricingPolicy(String code) {
        this.code = code;
    }
}
