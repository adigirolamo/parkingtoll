package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import com.adigi.parkingtoll.service.pricing.PricingStrategyFactory;
import com.adigi.parkingtoll.service.pricing.strategy.PricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PricingStrategyService {

    private LocalDateTimeService localDateTimeService;

    private PricingStrategyFactory pricingStrategyFactory;

    @Autowired
    public PricingStrategyService(LocalDateTimeService localDateTimeService, PricingStrategyFactory pricingStrategyFactory) {
        this.localDateTimeService = localDateTimeService;
        this.pricingStrategyFactory = pricingStrategyFactory;
    }

    /**
     * Calculate vehicle's bill amount, based on the parking pricing strategy
     *
     * @param parking
     * @param reservation related to the vehicle
     * @param now         paying time
     * @return amount that has to be paid
     */
    public BigDecimal calculateAmount(Parking parking, Reservation reservation, LocalDateTime now) {

        PricingStrategy pricingStrategy = pricingStrategyFactory.getStrategy(parking.getPricingPolicy());

        //Get time calculate
        Long minutesDifference = localDateTimeService.getMinutesDifference(
                reservation.getLocalArriveDateTime(),
                now);

        //execute algorithm
        BigDecimal amount = pricingStrategy.calculateAmount(
                parking.getFixedAmount(),
                parking.getMinutePrice(),
                minutesDifference);

        return amount;
    }
}
