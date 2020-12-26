package com.adigi.parkingtoll.algorithm.pricing;

import com.adigi.parkingtoll.algorithm.pricing.strategy.FeeAndHoursPricingStrategy;
import com.adigi.parkingtoll.algorithm.pricing.strategy.OnlyHoursPricingStrategy;
import com.adigi.parkingtoll.algorithm.pricing.strategy.PricingStrategy;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PricingStrategyFactory.class, FeeAndHoursPricingStrategy.class, OnlyHoursPricingStrategy.class})
class PricingStrategyFactoryUnitTest {

    @Autowired
    private PricingStrategyFactory pricingStrategyFactory;

    @Test
    public void givenONLY_HOURS_whenGetStrategy_getOnlyHoursPricingStrategy() {

        // given
        PricingPolicy pricingPolicy = PricingPolicy.ONLY_HOURS;

        // when
        PricingStrategy strategy = pricingStrategyFactory.getStrategy(pricingPolicy);

        // then
        assertNotNull(strategy);
        assertThat(strategy, instanceOf(OnlyHoursPricingStrategy.class));
        assertThat(strategy, not(instanceOf(FeeAndHoursPricingStrategy.class)));
    }


    @Test
    public void givenINITIAL_FEE_PLUS_HOURS_whenGetStrategy_getFeeAndHoursPricingStrategy() {

        // given
        PricingPolicy pricingPolicy = PricingPolicy.INITIAL_FEE_PLUS_HOURS;

        // when
        PricingStrategy strategy = pricingStrategyFactory.getStrategy(pricingPolicy);

        // then
        assertNotNull(strategy);
        assertThat(strategy, instanceOf(FeeAndHoursPricingStrategy.class));
        assertThat(strategy, not(instanceOf(OnlyHoursPricingStrategy.class)));
    }

}