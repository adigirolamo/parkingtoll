package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.presentation.dto.BillDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "Bill API", description = "it handles the request to get the vehicle's bill " +
        "and the request to update the bill to paid")
public interface BillAPI {

    // swagger
    @Operation(summary = "Get vehicle's bill",
            description = "Calculate the bill cost and return it. The bill is related to a vehicle. " +
                    " The bill calculation is based on the time that the vehicle has occupied the slot and " +
                    " the pricing strategy that the Toll parking has set. At the moment 2 strategies are suppoerted: " +
                    " price per minutes OR fixed amount plus price per minutes. " +
                    " This request represents the functional case when a vehicle is leaving" +
                    " and it goes to the parking's appliance (that is the API client) to pay." +
                    " The appliance recognizes the vehicle's plate" +
                    " and send the request to the BE that calculates the bill cost and return it to the appliance that displays it.")
    @ApiResponse(responseCode = "200", description = "Get bill to be paid. Amount indicates the value that has to be paid. " +
            "When successful Parking slot status changes to PAYING",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))})
    // swagger
    @GetMapping(value = "/parkings/{parkingNameUid}/parkingslots/{plate}/reservation/bill",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillDTO> calculateBillForLeavingCar(
            @Parameter(description = "Parking unique name. Use 'PARKING1' to try it") @PathVariable String parkingNameUid,
            @Parameter(description = "Plate of the vehicle that is leaving and that has to pay") @PathVariable String plate
    );

    // swagger
    @Operation(summary = "Update bill to paid", description = "It represents the functional case when" +
            " the vehicle has paid the bill using the appliance" +
            " and the appliance call the server to update the bill/parking slot status")
    @ApiResponse(responseCode = "200", description = "Update bill to paid." +
            " It will return the bill where its amount will be 0 (because it has been paid)" +
            " and the status will be PAID." +
            " When successful Parking slot status changes to PAID",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BillDTO.class))})
    // swagger
    @PutMapping(value = "/parkings/{parkingNameUid}/parkingslots/{plate}/reservation/bill",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillDTO> payBill(
            @Parameter(description = "Parking unique name. Use 'PARKING1' to try it") @PathVariable String parkingNameUid,
            @Parameter(description = "Plate of the vehicle that is leaving and that has paid the bill") @PathVariable String plate
    );
}
