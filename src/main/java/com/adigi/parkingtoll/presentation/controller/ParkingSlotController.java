package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import com.adigi.parkingtoll.service.ParkingSlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@Validated
public class ParkingSlotController extends BaseRestController<ParkingSlot, ParkingSlotDTO> implements ParkingSlotAPI {

    private ParkingSlotService parkingSlotService;

    @Autowired
    public ParkingSlotController(ParkingSlotService parkingSlotService, ModelMapper modelMapper) {
        this.parkingSlotService = parkingSlotService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ParkingSlotDTO> getParkingSlot(
            @PathVariable String parkingNameUid,
            @RequestParam("plate") @NotBlank @Size(max = 20) String plate,
            @RequestParam("engineType") EngineType engineType
    ) {
        ParkingSlot parkingSlot =
                parkingSlotService.getFreeParkingSlotByParkingAndEngineType(parkingNameUid, plate, engineType);

        return createResponse(parkingSlot, parkingNameUid);
    }

    @Override
    public ResponseEntity<ParkingSlotDTO> updateParkingSlotToFree(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId
    ) {
        ParkingSlot parkingSlot = parkingSlotService.updateParkingSlotToFree(parkingSlotId, parkingNameUid);

        return createResponse(parkingSlot, parkingNameUid);
    }

    @Override
    protected ParkingSlotDTO convertToDto(ParkingSlot parkingSlot, String... args) {
        ParkingSlotDTO parkingSlotDTO = modelMapper.map(parkingSlot, ParkingSlotDTO.class);
        parkingSlotDTO.setParkingNameUid(args[0]);

        return parkingSlotDTO;
    }
}
