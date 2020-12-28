package com.adigi.parkingtoll.service.pricing.strategy;


import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FeeAndHoursPricingStrategy extends PricingStrategy {

    public FeeAndHoursPricingStrategy() {

        pricingStrategyFunctional =
                (fixedAmount, pricePerMinute, minutesDifference)
                        -> pricePerMinute
                        .multiply(BigDecimal.valueOf(minutesDifference))
                        .add(fixedAmount);

    }

    @Override
    public PricingPolicy getType() {
        return PricingPolicy.INITIAL_FEE_PLUS_HOURS;
    }
}
