package com.adigi.parkingtoll.presentation.dto;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO related to Parking slot entity.
 * It represent a parking slot of a parking.
 * <ul>
 *     <li>A Parking slot has associated a reservation (One to One relation).</li>
 *     <li>Reservation has associated a bill (One to One relation).</li>
 * </ul>
 * Therefore there is a direct link between a parking slot, its reservation and its bill
 *
 * @see com.adigi.parkingtoll.model.persistence.entity.ParkingSlot
 */
@Setter
@Getter
public class ParkingSlotDTO {

    @Schema(example = "10", description = "")
    private Long id;

    /**
     * Parking slot position
     */
    @Schema(example = "A201", description = "")
    private String position;

    /**
     * In which floor is the parking slot
     */
    @Schema(example = "2", description = "")
    private Integer floor;

    /**
     * Vehicle type that the parking slot is designed for.
     *
     * @see VehicleType
     */
    @Schema(example = "CAR", description = "")
    private VehicleType vehicleType;

    /**
     * Engine type that the parking slot is designed for.
     * Ex. if a parking slot has engine type ELECTRIC_20KW, it could be reserved only for electric 20KW cars
     *
     * @see EngineType
     */
    @Schema(example = "GASOLINE", description = "Supported engine type GASOLINE, ELECTRIC_20KW, ELECTRIC_50KW")
    private EngineType engineType;

    /**
     * Parking UID name. The parking slot belongs to this parking.
     */
    @Schema(example = "PARKING1", description = "Parking unique names. For the demo they have been configured: PARKING1 and PARKING2")
    private String parkingNameUid;

    private Boolean reserved;

    /**
     * Current state of the parking slot
     *
     * @see ParkingSlotState
     */
    @Schema(example = "RESERVED", description = "Possible state FREE,RESERVED,PAYING,PAID")
    private ParkingSlotState parkingSlotState;
}
