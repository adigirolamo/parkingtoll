package com.adigi.parkingtoll.service.pricing.strategy;

import com.adigi.parkingtoll.model.enums.PricingPolicy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class PricingStrategy {

    public abstract PricingPolicy getType();

    PricingStrategyFunctional pricingStrategyFunctional;

    /**
     * Calculate the amount that has to be payed
     *
     * @param fixedAmount       fixed cost. Depending on the strategy chosen by the Parking, it could be not used
     * @param pricePerMinute    price per minute cost
     * @param minutesDifference difference between paying time and arrival time
     * @return amount to be payed
     */
    public BigDecimal calculateAmount(BigDecimal fixedAmount, BigDecimal pricePerMinute, Long minutesDifference) {
        return pricingStrategyFunctional
                .applyStrategy(fixedAmount, pricePerMinute, minutesDifference)
                .setScale(2, RoundingMode.DOWN);
    }

}
