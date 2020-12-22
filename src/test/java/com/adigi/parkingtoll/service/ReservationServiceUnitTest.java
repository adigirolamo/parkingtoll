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

    private final String PLATE_OLD = "OLD";
    private final String PLATE_NEW = "NEW";
    private LocalDateTime oldTime;

    private ParkingSlot parkingSlot;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        oldTime = LocalDateTime.of(2000, 1, 1, 1, 1);
        parkingSlot = new ParkingSlot();
        reservation = new Reservation();
        parkingSlot.setReservation(reservation);
    }

    @Test
    public void updateReservationPlateAndArrival_getUpdatedReservation() {

        // given
        reservation.setLocalArriveDateTime(oldTime);
        reservation.setPlate(PLATE_OLD);
        reservation.setPayed(true);

        // method to verify
        reservationService.updateReservationForIncomingCar(parkingSlot, PLATE_NEW);

        // verify
        assertEquals(reservation.getPlate(), PLATE_NEW);
        assertTrue(reservation.getLocalArriveDateTime().isAfter(oldTime));
        assertFalse(reservation.getPayed());
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

    @Test
    public void updateReservationDeparture_getUpdated() {

        // given
        reservation.setLocalDepartureDateTime(oldTime);

        // method to verify
        reservationService.updateReservationDeparture(parkingSlot);

        // verify
        assertTrue(reservation.getLocalDepartureDateTime().isAfter(oldTime));
    }
}
