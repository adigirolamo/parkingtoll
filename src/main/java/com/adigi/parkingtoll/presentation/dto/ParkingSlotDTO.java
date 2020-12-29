package com.adigi.parkingtoll.presentation.dto;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParkingSlotDTO {

    private Long id;

    private String position;

    private Integer floor;

    private VehicleType vehicleType;

    private EngineType engineType;

    private String parkingNameUid;

    private Boolean reserved;

    private ParkingSlotState parkingSlotState;
}
