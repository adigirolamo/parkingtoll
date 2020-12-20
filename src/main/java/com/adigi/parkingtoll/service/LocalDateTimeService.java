package com.adigi.parkingtoll.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LocalDateTimeService {

    public Long getMinutesDifference(LocalDateTime arrivalTime, LocalDateTime paymentTime) {
        Duration duration = Duration.between(arrivalTime, paymentTime);
        return duration.toMinutes();
    }
}
