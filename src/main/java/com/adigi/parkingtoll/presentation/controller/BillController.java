package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class BillController extends BaseRestController<Bill, BillDTO> implements BillAPI {

    private BillService billService;

    @Autowired
    public BillController(BillService billService, ModelMapper modelMapper) {
        this.billService = billService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<BillDTO> calculateBillForLeavingCar(
            @PathVariable String parkingNameUid,
            @PathVariable String plate
    ) {
        Bill bill = billService.calculateBillForLeavingCar(parkingNameUid, plate);

        return createResponse(bill);
    }

    @Override
    public ResponseEntity<BillDTO> payBill(
            @PathVariable String parkingNameUid,
            @PathVariable String plate
    ) {
        Bill bill = billService.payBill(parkingNameUid, plate);

        return createResponse(bill);
    }

    @Override
    protected BillDTO convertToDto(Bill bill, String... args) {
        ParkingSlot parkingSlot = bill.getReservation().getParkingSlot();

        BillDTO billDTO = modelMapper.map(bill, BillDTO.class);
        billDTO.setPlate(bill.getReservation().getPlate());
        billDTO.setParkingSlotState(parkingSlot.getParkingSlotState());
        billDTO.setParkingSlotId(parkingSlot.getId());

        return billDTO;
    }
}
