package com.adigi.parkingtoll.service.state;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
@Getter
@Setter
public class StateData {

    private String plate;

    private LocalDateTime localPaymentDateTime;
}
