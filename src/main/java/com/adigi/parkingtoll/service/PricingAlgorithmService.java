package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.algorithm.pricing.PricingAlgorithm;
import com.adigi.parkingtoll.algorithm.pricing.PricingAlgorithmFactory;
import com.adigi.parkingtoll.model.persistance.entity.Parking;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PricingAlgorithmService {

    @Autowired
    private LocalDateTimeService localDateTimeService;

    public BigDecimal calculateAmount(Parking parking, Reservation reservation, LocalDateTime now) {

        PricingAlgorithm pricingAlgorithm = PricingAlgorithmFactory.getPricingAlgorithm(parking.getPricingPolicy());

        //Get time calculate
        Long minutesDifference = localDateTimeService.getMinutesDifference(
                reservation.getLocalArriveDateTime(),
                now);

        //execute algoritm
        BigDecimal amount = pricingAlgorithm.calculateAmount(
                parking.getFixedAmount(),
                parking.getMinutePrice(),
                minutesDifference);

        return amount;
    }
}
