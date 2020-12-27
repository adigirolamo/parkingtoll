package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.Parking;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PricingStrategyService pricingStrategyService;

    @Autowired
    private LocalDateTimeService timeService;

    @Autowired
    private ExceptionService exceptionService;

    public void updateBillForIncomingCar(Reservation reservation) {

        Bill bill = getOrCreateReservationBill(reservation);
        resetBillAmount(bill);

        billRepository.save(bill);
    }

    public Bill payBill(String parkingNameUid, Long parkingSlotId, Long billId) {

        Bill bill = billRepository.retrieveByParkingNameParkingSlotIdBillId(parkingNameUid, parkingSlotId, billId);

        exceptionService.checkNotNull(bill,
                String.format("Bill id[%d] for slot [%d] and parking [%s]",
                        billId, parkingSlotId, parkingNameUid));

        resetBill(bill);

        reservationService.updateReservationPayed(bill.getReservation());

        return bill;
    }

    /**
     * reset bill amount
     *
     * @param bill
     */
    private void resetBill(Bill bill) {

        resetBillAmount(bill);
    }


    /**
     * Calculate Bill amount and return bill
     * To calculate the bill it is used the algoritm defined for the parking
     *
     * @param parkingNameUid
     * @param parkingSlotId
     * @return
     */
    public Bill calculateBillForLeavingCar(String parkingNameUid, Long parkingSlotId) {

        //retrieve bill
        Bill bill = retrieveBill(parkingNameUid, parkingSlotId);

        //TODO test error
        exceptionService.checkNotNull(bill,
                String.format("Bill for slot [%d] and parking [%s]",
                        parkingSlotId, parkingNameUid));

        //TODO remove direct localDateTime.now()
        LocalDateTime paymentTime = timeService.getNow();

        Reservation reservation = bill.getReservation();
        BigDecimal amount = pricingStrategyService.calculateAmount(retrieveParking(bill), reservation, paymentTime);

        reservationService.setPaymentTime(reservation, paymentTime);

        bill.setAmount(amount);

        return bill;
    }


    private Bill retrieveBill(String parkingNameUid, Long parkingSlotId) {
        return billRepository.findFirstByReservationParkingSlotIdAndReservationParkingSlotParkingNameUid(
                parkingSlotId,
                parkingNameUid
        );
    }

    private Parking retrieveParking(Bill bill) {
        return bill.getReservation().getParkingSlot().getParking();
    }

    private Bill getOrCreateReservationBill(Reservation reservation) {

        Bill bill = reservation.getBill();

        if (bill == null) {
            //TODO change to bibuilder
            bill = Bill.builder().prepareDefault().build();
            reservation.setBill(bill);
        }

        return bill;
    }

    private void resetBillAmount(Bill bill) {
        bill.setAmount(BigDecimal.ZERO);
    }
}
