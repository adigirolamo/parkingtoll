package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import com.adigi.parkingtoll.service.ParkingSlotService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@Validated
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/parking/{parkingNameUid}/parkingslot")
    public ResponseEntity<ParkingSlotDTO> getParkingSlot(
            @PathVariable String parkingNameUid,
            @RequestParam("plate") @NotBlank @Size(max = 20) String plate,
            @RequestParam("engineType") EngineType engineType
    ) {
        ParkingSlot parkingSlot =
                parkingSlotService.getFreeParkingSlotByParkingAndEngineType(parkingNameUid, plate, engineType);

        if (parkingSlot != null) {

            return new ResponseEntity<ParkingSlotDTO>(convertToDto(parkingSlot, parkingNameUid), HttpStatus.OK);
        } else {
            return new ResponseEntity<ParkingSlotDTO>(new ParkingSlotDTO(), HttpStatus.NOT_FOUND);
        }

    }

    private ParkingSlotDTO convertToDto(ParkingSlot parkingSlot, String parkingNameUid) {

        ParkingSlotDTO parkingSlotDTO = modelMapper.map(parkingSlot, ParkingSlotDTO.class);
        parkingSlotDTO.setParkingNameUid(parkingNameUid);

        return parkingSlotDTO;
    }
}
