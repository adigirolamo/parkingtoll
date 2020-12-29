package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import com.adigi.parkingtoll.service.ParkingSlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@Validated
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private ModelMapper modelMapper;

    //TODO add swagger description
    @GetMapping(value = "/parking/{parkingNameUid}/parkingslot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingSlotDTO> getParkingSlot(
            @PathVariable String parkingNameUid,
            @RequestParam("plate") @NotBlank @Size(max = 20) String plate,
            @RequestParam("engineType") EngineType engineType
    ) {
        ParkingSlot parkingSlot =
                parkingSlotService.getFreeParkingSlotByParkingAndEngineType(parkingNameUid, plate, engineType);

        return createResponse(parkingSlot, parkingNameUid);
    }

    @PutMapping(value = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingSlotDTO> updateParkingSlotToFree(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId
    ) {
        ParkingSlot parkingSlot = parkingSlotService.updateParkingSlotToFree(parkingSlotId, parkingNameUid);

        return createResponse(parkingSlot, parkingNameUid);
    }

    //TODO it is possible to refactor it, with a general method that accepts a convertToDto and a map ? or String ...
    private ResponseEntity<ParkingSlotDTO> createResponse(ParkingSlot parkingSlot, String parkingNameUid) {

        return new ResponseEntity<>(convertToDto(parkingSlot, parkingNameUid), HttpStatus.OK);
    }

    private ParkingSlotDTO convertToDto(ParkingSlot parkingSlot, String parkingNameUid) {

        ParkingSlotDTO parkingSlotDTO = modelMapper.map(parkingSlot, ParkingSlotDTO.class);
        parkingSlotDTO.setParkingNameUid(parkingNameUid);

        return parkingSlotDTO;
    }
}
