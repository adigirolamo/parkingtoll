package com.adigi.parkingtoll.algorithm.pricing;

import com.adigi.parkingtoll.exception.NotDefiniedAlgorithmException;
import com.adigi.parkingtoll.model.enums.PricingPolicy;

public class PricingAlgorithmFactory {

    public static PricingAlgorithm getPricingAlgorithm(PricingPolicy pricingPolicy) {

        PricingAlgorithm pricingAlgorithm;

        ////TODO Change to map e farlo bean e farlo postconstruct
        switch (pricingPolicy) {
            case ONLY_HOURS:
                pricingAlgorithm = PricingAlgorithm.onlyHoursPricing();
                break;

            case INITIAL_FEE_PLUS_HOURS:
                pricingAlgorithm = PricingAlgorithm.feeAndHoursPricing();
                break;

            default:
                throw new NotDefiniedAlgorithmException("PricingAlgorithm has not been definied");
        }

        return pricingAlgorithm;
    }
}
