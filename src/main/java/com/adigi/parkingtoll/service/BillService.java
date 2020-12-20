package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public void updateBillForIncomingCar(Reservation reservation) {

        Bill bill = getOrCreateReservationBill(reservation);
        resetBillAmount(bill);

        billRepository.save(bill);
    }

    //calculate Bill amount

    public Bill getOrCreateReservationBill(Reservation reservation) {

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
