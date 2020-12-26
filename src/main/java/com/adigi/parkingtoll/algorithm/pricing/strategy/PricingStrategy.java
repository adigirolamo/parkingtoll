package com.adigi.parkingtoll.algorithm.pricing.strategy;

import com.adigi.parkingtoll.model.enums.PricingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class PricingStrategy {

    public abstract PricingPolicy getType();

    PricingStrategyFunctional pricingStrategyFunctional;

    //TODO search for java functional interface
    //TODO create a java object that contains the values
    public BigDecimal calculateAmount(BigDecimal fixedAmount, BigDecimal pricePerMinute, Long minutesDifference) {
        return pricingStrategyFunctional
                .applyStrategy(fixedAmount, pricePerMinute, minutesDifference)
                .setScale(2, RoundingMode.DOWN);
    }

}
