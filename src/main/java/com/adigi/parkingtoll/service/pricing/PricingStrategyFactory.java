package com.adigi.parkingtoll.service.pricing;

import com.adigi.parkingtoll.service.pricing.strategy.PricingStrategy;
import com.adigi.parkingtoll.exception.NotDefiniedAlgorithmException;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PricingStrategyFactory {

    private static final Map<PricingPolicy, PricingStrategy> myStrategyCache = new HashMap<>();

    @Autowired
    private PricingStrategyFactory(List<PricingStrategy> algorithms) {
        for (PricingStrategy algorithm : algorithms) {
            myStrategyCache.put(algorithm.getType(), algorithm);
        }
    }

    public PricingStrategy getStrategy(PricingPolicy type) {

        PricingStrategy strategy = myStrategyCache.get(type);

        if (strategy == null) {
            throw new NotDefiniedAlgorithmException("PricingAlgorithm has not been definied");
        }

        return strategy;
    }
}
