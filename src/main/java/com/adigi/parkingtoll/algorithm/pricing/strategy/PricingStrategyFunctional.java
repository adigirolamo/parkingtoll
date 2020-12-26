package com.adigi.parkingtoll.algorithm.pricing.strategy;

import java.math.BigDecimal;

@FunctionalInterface
public interface PricingStrategyFunctional {

    BigDecimal applyStrategy(BigDecimal fixedAmount, BigDecimal pricePerMinute, Long minutesDifference);
}
