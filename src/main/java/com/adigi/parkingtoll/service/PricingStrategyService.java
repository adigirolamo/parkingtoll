package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.service.pricing.PricingStrategyFactory;
import com.adigi.parkingtoll.service.pricing.strategy.PricingStrategy;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PricingStrategyService {

    @Autowired
    private LocalDateTimeService localDateTimeService;

    @Autowired
    private PricingStrategyFactory pricingStrategyFactory;

    public BigDecimal calculateAmount(Parking parking, Reservation reservation, LocalDateTime now) {

        PricingStrategy pricingStrategy = pricingStrategyFactory.getStrategy(parking.getPricingPolicy());

        //Get time calculate
        Long minutesDifference = localDateTimeService.getMinutesDifference(
                reservation.getLocalArriveDateTime(),
                now);

        //execute algoritm
        BigDecimal amount = pricingStrategy.calculateAmount(
                parking.getFixedAmount(),
                parking.getMinutePrice(),
                minutesDifference);

        return amount;
    }
}
