package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
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

    @GetMapping(value = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}/reservation/bill", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillDTO> calculateBillForLeavingCar(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId
    ) {
        Bill bill = billService.calculateBillForLeavingCar(parkingNameUid, parkingSlotId);

        return createResponse(bill);
    }

    @PutMapping(value = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}/reservation/bill/{billId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
