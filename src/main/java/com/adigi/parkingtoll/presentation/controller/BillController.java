package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}/reservation/bill")
    public ResponseEntity<BillDTO> calculateBillForLeavingCar(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId
    ) {
        Bill bill = billService.calculateBillForLeavingCar(parkingNameUid, parkingSlotId);

        ResponseEntity<BillDTO> response = createResponse(bill);
        return response;
    }

    @PutMapping(value = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}/reservation/bill/{billId}")
    public ResponseEntity<BillDTO> payBill(
            @PathVariable String parkingNameUid,
            @PathVariable Long parkingSlotId,
            @PathVariable Long billId
    ) {
        Bill bill = billService.payBill(parkingNameUid, parkingSlotId, billId);

        ResponseEntity<BillDTO> response = createResponse(bill);
        return response;
    }

    private ResponseEntity<BillDTO> createResponse(Bill bill) {
        if (bill != null) {

            return new ResponseEntity<BillDTO>(convertToDto(bill), HttpStatus.OK);
        } else {
            return new ResponseEntity<BillDTO>(new BillDTO(), HttpStatus.NOT_FOUND);
        }
    }

    private BillDTO convertToDto(Bill bill) {

        BillDTO billDTO = modelMapper.map(bill, BillDTO.class);
        billDTO.setPlate(bill.getReservation().getPlate());

        return billDTO;
    }
}
