package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.persistance.entity.Parking;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PricingAlgorithmServiceUnitTest {

    @Mock
    private LocalDateTimeService localDateTimeService;

    @InjectMocks
    private PricingAlgorithmService pricingAlgorithmService;

    private Parking parking = new Parking();
    private Reservation reservation = new Reservation();

    @Test
    public void onlyHoursPricing() {

        // give
        Long minutesDifference = 7L;

        parking.setPricingPolicy(PricingPolicy.ONLY_HOURS);
        parking.setFixedAmount(new BigDecimal(500));
        parking.setMinutePrice(new BigDecimal(0.1));

        Mockito.when(
                localDateTimeService
                        .getMinutesDifference(
                                any(), any())).thenReturn(minutesDifference);

        // when
        BigDecimal amount = pricingAlgorithmService.calculateAmount(parking, reservation, null);

        // verify
        BigDecimal expectedResult = new BigDecimal(minutesDifference * 0.1)
                .setScale(2, RoundingMode.DOWN);
        assertEquals(amount, expectedResult);
    }

    //algoritmo 2
    public void feeAndHoursPricing() {

        // give
        Long minutesDifference = 7L;
        BigDecimal initialFee = new BigDecimal(23);

        parking.setPricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS);
        parking.setFixedAmount(new BigDecimal(23));
        parking.setMinutePrice(new BigDecimal(0.1));

        Mockito.when(
                localDateTimeService
                        .getMinutesDifference(
                                any(), any())).thenReturn(minutesDifference);

        // when
        BigDecimal amount = pricingAlgorithmService.calculateAmount(parking, reservation, null);

        // verify
        BigDecimal expectedResult = new BigDecimal(minutesDifference * 0.1)
                .add(initialFee)
                .setScale(2, RoundingMode.DOWN);
        assertEquals(amount, expectedResult);
    }
}
