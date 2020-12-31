package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Tag(name = "Parking slot API", description = "it handles the request to get a FREE parking slot for an incoming vehicle." +
        " The returned parking slot will be specific for the vehicle engine type." +
        " It also handles the request to update the parking slot to FREE when a vehicle leaves")
public interface ParkingSlotAPI {

    //swagger
    @Operation(summary = "Get FREE parking slot",
            description = "Get FREE parking slot for an incoming vehicle and assigns the parking slot to it, doing so the parking slot will be marked as reserved." +
                    " It represents the functional case when a vehicle is entering the parking, the parking's appliance (that is the client) detects the vehicle's plate " +
                    " and asks the BE to get a FREE parking slot for the vehicle, taking into consideration vehicle's engine type. ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get a FREE parking slot. The parking slot status will be Reserved, because the BE retrieves a FREE slot" +
            " and assigns it to the vehicle. " +
            " When successful Parking slot status changes to RESERVED",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSlotDTO.class))}),
            @ApiResponse(responseCode = "404", description = "When 404 is returned, for this request, it indicates that there is not a free parking slot" +
                    " of the requested engine type, in the requested toll parking.")})
    //swagger
    @GetMapping(value = "/parkings/{parkingNameUid}/parkingslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingSlotDTO> getParkingSlot(
            @Parameter(description = "Parking unique name. Use 'PARKING1' to try it") @PathVariable String parkingNameUid,
            @Parameter(description = "Vehicle's plate. It has to be unique") @RequestParam("plate") @NotBlank @Size(max = 20) String plate,
            @Parameter(description = "Vehicle's plate engine type") @RequestParam("engineType") EngineType engineType
    );

    //swagger
    @Operation(summary = "Update parking slot to FREE",
            description = "It represents the functional case when the vehicle has already paid the bill and it is leaving the parking toll. " +
                    " This request will be fired by the appliance, after that the server has responded its previous request (update bill to paid). " +
                    " When the BE responds with success, the appliances raises the parking bar")
    @ApiResponse(responseCode = "200", description = "Update the parking slot to FREE" +
            " When successful Parking slot status changes to FREE",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSlotDTO.class))})
    //swagger
    @PutMapping(value = "/parkings/{parkingNameUid}/parkingslots/{parkingSlotId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingSlotDTO> updateParkingSlotToFree(
            @Parameter(description = "Parking unique name. Use 'PARKING1' to try it") @PathVariable String parkingNameUid,
            @Parameter(description = "Parking slot ID") @PathVariable Long parkingSlotId
    );
}
