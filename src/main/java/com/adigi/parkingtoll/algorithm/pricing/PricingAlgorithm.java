package com.adigi.parkingtoll.algorithm.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface PricingAlgorithm {

    //TODO search for java functional interface
    //TODO create a java object that contains the values
    BigDecimal calculateAmount(BigDecimal fixedAmount, BigDecimal pricePerMinute, Long minutesDifference);

    static PricingAlgorithm onlyHoursPricing() {
        return (fixedAmount, pricePerMinute, minutesDifference)
                -> pricePerMinute
                .multiply(BigDecimal.valueOf(minutesDifference))
                .setScale(2, RoundingMode.DOWN);
    }

    static PricingAlgorithm feeAndHoursPricing() {
        return (fixedAmount, pricePerMinute, minutesDifference)
                -> pricePerMinute
                .multiply(BigDecimal.valueOf(minutesDifference))
                .add(fixedAmount)
                .setScale(2, RoundingMode.DOWN);
    }
}
