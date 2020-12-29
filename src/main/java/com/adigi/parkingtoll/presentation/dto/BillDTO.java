package com.adigi.parkingtoll.presentation.dto;

import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BillDTO {

    private long id;

    private Currency currency;

    private BigDecimal amount;

    private String plate;

    private ParkingSlotState parkingSlotState;

}
