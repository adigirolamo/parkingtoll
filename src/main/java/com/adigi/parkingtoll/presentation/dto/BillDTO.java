package com.adigi.parkingtoll.presentation.dto;

import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO of Bill entity.
 * It represents a bill related to a Parking slot.
 * The relation chain is the following:
 * <ul>
 *     <li>Parking slot has one to one relation with reservation</li>
 *     <li>reservation has one to one relation with bill</li>
 * </ul>
 * Therefore when a vehicle is using a parking slot, billDTO will be related to that vehicle.
 *
 * @see com.adigi.parkingtoll.model.persistence.entity.Bill
 */
@Setter
@Getter
public class BillDTO {

    @Schema(example = "10", description = "")
    private long id;

    /**
     * Currency
     *
     * @see Currency
     */
    @Schema(example = "EURO", description = "")
    private Currency currency;

    /**
     * Amount that has to be paid by the vehicle.
     * It could be 0.
     */
    @Schema(example = "10.20", description = "it is 0 if there is no vehicle using the parking slot or if the vehicle has paid it")
    private BigDecimal amount;

    /**
     * Vehicle's plate. The bill is related this vehicle.
     * It can be null when there isn't any vehicle using the parking slot
     */
    @Schema(example = "plate1", description = "Vehicle's plate. The bill is related this vehicle. It can be null when there isn't any vehicle using the parking slot")
    private String plate;

    /**
     * Current state of the parking slot
     *
     * @see ParkingSlotState
     */
    @Schema(example = "PAYING", description = "Possible state FREE,RESERVED,PAYING,PAID")
    private ParkingSlotState parkingSlotState;

    @Schema(example = "10", description = "")
    private long parkingSlotId;

}
