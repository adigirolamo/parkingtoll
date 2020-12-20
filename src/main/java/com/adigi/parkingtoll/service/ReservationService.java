package com.adigi.parkingtoll.service;

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
     * Update plate and localArriveDateTime of parkingSlot's reservation
     * localArriveDateTime is set as LocalDateTime.now
     *
     * @param parkingSlot
     * @param plate
     */
    public void updateReservationPlateAndArrival(ParkingSlot parkingSlot, String plate) {

        Reservation reservation = getOrCreateParkingSlotReservation(parkingSlot);
        reservation.setPlate(plate);
        reservation.setLocalArriveDateTime(LocalDateTime.now());

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
            reservation = new Reservation(parkingSlot, LocalDateTime.now());
            parkingSlot.setReservation(reservation); //TODO verify if needed, it should not be needed
        }

        return reservation;
    }
}
