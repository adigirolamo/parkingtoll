package com.adigi.parkingtoll.service.state;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class StateData {

    private String plate;

    private LocalDateTime localPaymentDateTime;
}
