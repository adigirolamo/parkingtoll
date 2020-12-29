package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.repository.BillRepository;
import com.adigi.parkingtoll.service.state.ParkingSlotStateService;
import com.adigi.parkingtoll.service.state.StateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.PAYED;
import static com.adigi.parkingtoll.model.enums.ParkingSlotState.PAYING;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private LocalDateTimeService timeService;

    @Autowired
    private ParkingSlotStateService parkingSlotStateService;

    @Autowired
    private ExceptionService exceptionService;

    public Bill payBill(String parkingNameUid, Long parkingSlotId, Long billId) {

        Bill bill = billRepository.retrieveByParkingNameParkingSlotIdBillId(parkingNameUid, parkingSlotId, billId);

        exceptionService.checkNotNull(bill,
                String.format("Bill id[%d] for slot [%d] and parking [%s]", billId, parkingSlotId, parkingNameUid));

        parkingSlotStateService.changeState(bill, PAYED, StateData.builder().build());

        return bill;
    }

    /**
     * Calculate Bill amount and return bill
     * To calculate the bill it is used the algorithm defined for the parking
     *
     * @param parkingNameUid
     * @param parkingSlotId
     * @return
     */
    public Bill calculateBillForLeavingCar(String parkingNameUid, Long parkingSlotId) {

        Bill bill = retrieveBill(parkingNameUid, parkingSlotId);

        exceptionService.checkNotNull(bill,
                String.format("Bill for slot [%d] and parking [%s]", parkingSlotId, parkingNameUid));

        parkingSlotStateService.changeState(bill, PAYING, StateData.builder().localPaymentDateTime(timeService.getNow()).build());

        return bill;
    }


    private Bill retrieveBill(String parkingNameUid, Long parkingSlotId) {
        return billRepository.findFirstByReservationParkingSlotIdAndReservationParkingSlotParkingNameUid(
                parkingSlotId,
                parkingNameUid
        );
    }

}
