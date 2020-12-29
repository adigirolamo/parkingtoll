package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private ModelMapper modelMapper;

    //TODO this is case car is going to leave, parking appliance (client) recognize plate
    // and asks BE to get the bill with calculate amount (PAYING state)
    //TODO change to plate from PSID . Client show amount
    @GetMapping(value = "/parkings/{parkingNameUid}/parkingslots/{parkingSlotId}/reservation/bill", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillDTO> calculateBillForLeavingCar(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId
    ) {
        Bill bill = billService.calculateBillForLeavingCar(parkingNameUid, parkingSlotId);

        return createResponse(bill);
    }

    //TODO this is the case after person has payed the amount. Appliance (client) send BE PUT bill payed for plate
    // BE answers with bill status and at this point the client get Parking Slot ID and asks BE to free that PS
    //with call updateParkingSlotToFree of PSC
    //TODO change to plate
    @PutMapping(value = "/parkings/{parkingNameUid}/parkingslots/{parkingSlotId}/reservation/bill/{billId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillDTO> payBill(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId,
            @PathVariable Long billId
    ) {
        Bill bill = billService.payBill(parkingNameUid, parkingSlotId, billId);

        return createResponse(bill);
    }

    private ResponseEntity<BillDTO> createResponse(Bill bill) {

        return new ResponseEntity<>(convertToDto(bill), HttpStatus.OK);
    }

    private BillDTO convertToDto(Bill bill) {

        BillDTO billDTO = modelMapper.map(bill, BillDTO.class);
        billDTO.setPlate(bill.getReservation().getPlate());
        billDTO.setParkingSlotState(bill.getReservation().getParkingSlot().getParkingSlotState());

        return billDTO;
    }
}
