package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUnitTest {


    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    final String PLATE_OLD = "OLD";
    final String PLATE_NEW = "NEW";
    LocalDateTime oldTime;

    @BeforeEach
    public void setUp() {
        oldTime = LocalDateTime.of(2000, 1, 1, 1, 1);
    }

    @Test
    public void updateReservationPlateAndArrival_getUpdatedReservation() {

        // given
        ParkingSlot parkingSlot = new ParkingSlot();
        Reservation reservation = new Reservation();
        parkingSlot.setReservation(reservation);

        reservation.setLocalArriveDateTime(oldTime);
        reservation.setPlate(PLATE_OLD);

        // method to verify
        reservationService.updateReservationPlateAndArrival(parkingSlot, PLATE_NEW);

        // verify
        assertEquals(reservation.getPlate(), PLATE_NEW);
        assertTrue(reservation.getLocalArriveDateTime().isAfter(oldTime));
    }

    @Test
    public void getOrCreateParkingSlotReservation_whenEmptyReservation_reservationIsCreated() {

        // given
        ParkingSlot parkingSlot = new ParkingSlot();

        // method to verify
        Reservation reservation = reservationService.getOrCreateParkingSlotReservation(parkingSlot);

        // verify
        assertNotNull(reservation);
    }
}
