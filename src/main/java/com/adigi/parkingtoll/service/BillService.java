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
public class BillService extends BaseControllerService{

    private BillRepository billRepository;

    private LocalDateTimeService timeService;

    @Autowired
    public BillService(BillRepository billRepository, LocalDateTimeService timeService, ParkingSlotStateService parkingSlotStateService, ExceptionService exceptionService) {
        this.billRepository = billRepository;
        this.timeService = timeService;
        this.parkingSlotStateService = parkingSlotStateService;
        this.exceptionService = exceptionService;
    }

    /**
     * Pay vehicle's bill. It changes the parking slot state to PAYED
     *
     * @param parkingNameUid parking unique name
     * @param plate          plate of the vehicle that has payed the bill
     * @return payed Bill
     * @see ParkingSlotStateService check status changes Business Logic
     */
    public Bill payBill(String parkingNameUid, String plate) {

        Bill bill = billRepository.retrieveByParkingNameAndPlate(parkingNameUid, plate);

        exceptionService.checkNotNull(bill,
                String.format("Bill for car [%s] and parking [%s]", plate, parkingNameUid));

        parkingSlotStateService.changeState(bill, PAYED, StateData.builder().build());

        return bill;
    }

    /**
     * Calculate Bill amount and return bill
     * To calculate the bill it is used the algorithm defined for the parking
     *
     * @param parkingNameUid parking unique name
     * @param plate          plate of the vehicle that request the bill
     * @return bill that has to be payed
     * @see ParkingSlotStateService check status changes Business Logic
     */
    public Bill calculateBillForLeavingCar(String parkingNameUid, String plate) {

        Bill bill = retrieveBill(parkingNameUid, plate);

        exceptionService.checkNotNull(bill,
                String.format("Bill for plate [%s] and parking [%s]", plate, parkingNameUid));

        parkingSlotStateService.changeState(bill, PAYING, StateData.builder().localPaymentDateTime(timeService.getNow()).build());

        return bill;
    }

    private Bill retrieveBill(String parkingNameUid, String plate) {
        return billRepository.findFirstByReservationParkingSlotParkingNameUidAndReservationPlate(parkingNameUid, plate);
    }

}
