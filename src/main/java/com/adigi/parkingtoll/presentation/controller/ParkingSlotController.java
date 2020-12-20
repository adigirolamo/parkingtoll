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
        List<ParkingSlot> parkingslots = parkingSlotService.findByParkingNameUid(parkingNameUid);

        //TODO
        // Se popolata ok
        // Se nulla invoca Service ParkingSlotDTO e gli chiede null DTO e setta status a 403
        return new ResponseEntity<ParkingSlotDTO>(convertToDto(parkingslots.get(0)), HttpStatus.OK);
    }


    private ParkingSlotDTO convertToDto(ParkingSlot parkingSlot) {

        ParkingSlotDTO parkingSlotDTO = modelMapper.map(parkingSlot, ParkingSlotDTO.class);
        return parkingSlotDTO;
    }
}
