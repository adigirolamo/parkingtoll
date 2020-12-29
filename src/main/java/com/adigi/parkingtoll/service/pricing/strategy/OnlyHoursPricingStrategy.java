package com.adigi.parkingtoll.service.pricing.strategy;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OnlyHoursPricingStrategy extends PricingStrategy {

    public OnlyHoursPricingStrategy() {

        pricingStrategyFunctional =
                (fixedAmount, pricePerMinute, minutesDifference)
                        -> pricePerMinute
                        .multiply(BigDecimal.valueOf(minutesDifference))
        ;
    }


    @Override
    public PricingPolicy getType() {
        return PricingPolicy.ONLY_HOURS;
    }
}
