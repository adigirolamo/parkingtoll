package com.adigi.parkingtoll.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LocalDateTimeServiceUnitTest {

    @InjectMocks
    private LocalDateTimeService localDateTimeService;

    private LocalDateTime start;
    private LocalDateTime end;

    @Test
    public void getMinutesDifference_deltaTimeOneDay_get1440() {

        // given
        start = LocalDateTime.now();
        end = start.plusDays(1);

        // when
        Long minutesDifference = localDateTimeService.getMinutesDifference(start, end);

        // verify
        assertEquals(minutesDifference, 24 * 60);
    }

    @Test
    public void getMinutesDifference_delta63Second_get1() {

        // given
        start = LocalDateTime.now();
        end = start.plusSeconds(63);

        // when
        Long minutesDifference = localDateTimeService.getMinutesDifference(start, end);

        // verify
        assertEquals(minutesDifference, 1);
    }

    @Test
    public void getMinutesDifference_delta5Minute45Second_get5() {

        // given
        start = LocalDateTime.now();
        end = start.plusMinutes(5).plusSeconds(45);

        // when
        Long minutesDifference = localDateTimeService.getMinutesDifference(start, end);

        // verify
        assertEquals(minutesDifference, 5);
    }
}
