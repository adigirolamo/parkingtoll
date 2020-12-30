package com.adigi.parkingtoll.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LocalDateTimeService {

    /**
     * Calculate time difference in minutes
     *
     * @param arrivalTime starting time
     * @param paymentTime ending time
     * @return differences in minutes
     */
    public Long getMinutesDifference(LocalDateTime arrivalTime, LocalDateTime paymentTime) {
        Duration duration = Duration.between(arrivalTime, paymentTime);
        return duration.toMinutes();
    }

    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
