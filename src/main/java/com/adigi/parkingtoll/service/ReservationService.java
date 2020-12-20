package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Update Reservation for and incoming car:
     * plate is set to the car plate
     * payed is set to false
     * arrival is set to now
     * departure is set to null
     *
     * @param parkingSlot
     * @param plate
     */
    public void updateReservationForIncomingCar(ParkingSlot parkingSlot, String plate) {

        Reservation reservation = getOrCreateParkingSlotReservation(parkingSlot);

        reservation.setPlate(plate);
        reservation.setPayed(false);
        reservation.setLocalArriveDateTime(LocalDateTime.now());
        reservation.setLocalDepartureDateTime(null);
        reservation.setLocalPaymentDateTime(null);

        reservationRepository.save(reservation);
    }

    public void updateReservationDeparture(ParkingSlot parkingSlot) {

        Reservation reservation = parkingSlot.getReservation();

        reservation.setLocalDepartureDateTime(LocalDateTime.now());
        reservation.setPlate(null);

        reservationRepository.save(reservation);
    }

    /**
     * return Reservation associated to a ParkingSlot.
     * If ParkingSlot has not a reservation associated yet, the reservation will be created and after returned
     *
     * @param parkingSlot
     * @return
     */
    public Reservation getOrCreateParkingSlotReservation(ParkingSlot parkingSlot) {

        Reservation reservation = parkingSlot.getReservation();

        if (reservation == null) {
            reservation = buildReservation(parkingSlot);
            parkingSlot.setReservation(reservation); //TODO verify if needed, it should not be needed
        }

        return reservation;
    }

    public void setPaymentTime(Reservation reservation, LocalDateTime paymentTime) {
        reservation.setLocalPaymentDateTime(paymentTime);
        reservationRepository.save(reservation);
    }

    private Reservation buildReservation(ParkingSlot parkingSlot) {
        Reservation reservation = new Reservation(parkingSlot, LocalDateTime.now());

        Bill bill = new Bill();
        setEachOtherDependencies(reservation, bill);

        return reservation;
    }

    private void setEachOtherDependencies(Reservation reservation, Bill bill) {
        bill.setReservation(reservation);
        reservation.setBill(bill);
    }
}
