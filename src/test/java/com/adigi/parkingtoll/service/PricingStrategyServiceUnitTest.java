package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.service.pricing.PricingStrategyFactory;
import com.adigi.parkingtoll.service.pricing.strategy.FeeAndHoursPricingStrategy;
import com.adigi.parkingtoll.service.pricing.strategy.OnlyHoursPricingStrategy;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PricingStrategyServiceUnitTest {

    @Mock
    private LocalDateTimeService localDateTimeService;

    @Mock
    private PricingStrategyFactory pricingStrategyFactory;

    @InjectMocks
    private PricingStrategyService pricingStrategyService;

    private Parking parking;
    private Long minutesDifference;
    private BigDecimal pricePerMinute;
    private BigDecimal initialFee;

    @BeforeEach
    public void setUp() {
        minutesDifference = 7L;
        pricePerMinute = new BigDecimal(0.1);
        initialFee = new BigDecimal(23);

        mockLocalDateTimeService();
    }


    private void mockLocalDateTimeService() {
        Mockito.when(localDateTimeService.getMinutesDifference(any(), any()))
                .thenReturn(minutesDifference);
    }

    @Test
    public void onlyHoursPricing() {

        // given
        parking = Parking.builder()
                .pricingPolicy(PricingPolicy.ONLY_HOURS)
                .fixedAmount(new BigDecimal(500))
                .minutePrice(pricePerMinute).build();

        // when
        Mockito.when(pricingStrategyFactory.getStrategy(PricingPolicy.ONLY_HOURS))
                .thenReturn(new OnlyHoursPricingStrategy());

        BigDecimal amount = pricingStrategyService.calculateAmount(parking, new Reservation(), null);

        // verify
        assertEquals(amount, calculatePricePerHours(minutesDifference, pricePerMinute, null));
    }

    @Test
    public void feeAndHoursPricing() {

        // given
        parking = Parking.builder()
                .pricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS)
                .fixedAmount(initialFee)
                .minutePrice(pricePerMinute).build();

        // when
        Mockito.when(pricingStrategyFactory.getStrategy(PricingPolicy.INITIAL_FEE_PLUS_HOURS))
                .thenReturn(new FeeAndHoursPricingStrategy());

        BigDecimal amount = pricingStrategyService.calculateAmount(parking, new Reservation(), null);

        // verify
        assertEquals(amount, calculateFeePlusPricePerHours(minutesDifference, pricePerMinute, initialFee));
        assertNotEquals(amount, calculatePricePerHours(minutesDifference, pricePerMinute, null));
    }

    private BigDecimal calculatePricePerHours(Long minutesDifference,
                                              BigDecimal pricePerMinute,
                                              BigDecimal initialFee) {

        return new BigDecimal(minutesDifference)
                .multiply(pricePerMinute)
                .setScale(2, RoundingMode.DOWN);
    }


    private BigDecimal calculateFeePlusPricePerHours(Long minutesDifference,
                                                     BigDecimal pricePerMinute,
                                                     BigDecimal initialFee) {

        return new BigDecimal(minutesDifference)
                .multiply(pricePerMinute)
                .add(initialFee)
                .setScale(2, RoundingMode.DOWN);
    }
}
