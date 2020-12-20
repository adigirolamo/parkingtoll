package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
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
    private PricingAlgorithmService pricingAlgorithmService;

    public void updateBillForIncomingCar(Reservation reservation) {

        Bill bill = getOrCreateReservationBill(reservation);
        resetBillAmount(bill);

        billRepository.save(bill);
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

        LocalDateTime paymentTime = LocalDateTime.now();

        Reservation reservation = bill.getReservation();
        BigDecimal amount = pricingAlgorithmService.calculateAmount(retrieveParking(bill), reservation, paymentTime);

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

    //TODO vedere se eliminarla
    private PricingPolicy retrievePricingPolicy(Bill bill) {
        return bill.getReservation().getParkingSlot().getParking().getPricingPolicy();
    }

    private Bill getOrCreateReservationBill(Reservation reservation) {

        Bill bill = reservation.getBill();

        if (bill == null) {
            bill = new Bill();
            reservation.setBill(bill); //TODO verify if needed, it should not be needed
        }

        return bill;
    }

    private void resetBillAmount(Bill bill) {
        bill.setAmount(BigDecimal.ZERO);
    }
}
