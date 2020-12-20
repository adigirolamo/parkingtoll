package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSlotServiceUnitTest {

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ParkingSlotService parkingSlotService;

    /**
     * Test method getFreeParkingSlotByParkingAndEngineType when the list - that is returned my ParkingSlotRepository -
     * is populated and verify that the returned PS is updated as occupied
     */
    @Test
    public void getFreeParkingSlotByParkingAndEngineType_getUpdatedParkingSlot() {

        // given
        List<ParkingSlot> parkingSlots = new ArrayList<>(Arrays.asList(new ParkingSlot()));

        // when
        Mockito.when(
                parkingSlotRepository
                        .findByParkingNameUidAndEngineTypeAndOccupiedFalse(
                                "ParkingUI", EngineType.GASOLINE)).thenReturn(parkingSlots);

        // method to verify
        ParkingSlot retrievedParkingSlot = parkingSlotService.getFreeParkingSlotByParkingAndEngineType(
                "ParkingUI", "PLATE", EngineType.GASOLINE);

        // verify
        assertTrue(retrievedParkingSlot.isOccupied());
    }

    @Test
    public void getFreeParkingSlotByParkingAndEngineType_whenEmptyList_getNull() {

        // when
        Mockito.when(
                parkingSlotRepository
                        .findByParkingNameUidAndEngineTypeAndOccupiedFalse(
                                "ParkingUI", EngineType.GASOLINE)).thenReturn(Collections.emptyList());

        // method to verify
        ParkingSlot retrievedParkingSlot = parkingSlotService.getFreeParkingSlotByParkingAndEngineType(
                "ParkingUI", "PLATE", EngineType.GASOLINE);

        // verify
        assertNull(retrievedParkingSlot);
    }

    @Test
    public void updateFirstParkingSlotAndReservation_onlyFirstIsOccupied() {

        // given
        List<ParkingSlot> slots = createNotOccupiedParkingSlots(3);


        // method to verify
        parkingSlotService.updateFirstParkingSlotAndReservation(slots, "TARGA");

        // verify
        assertTrue(slots.get(0).isOccupied());
        assertFalse(slots.get(1).isOccupied());
    }

    //TODO updateParkingSlotToFree
    // if parkingSlot returned
    // setOccupied == false
    // Check if update the test and control reservation too

    //TODO updateParkingSlotToFree
    // if not returned no error

    private List<ParkingSlot> createNotOccupiedParkingSlots(int num) {

        List<ParkingSlot> parkingSlots = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            ParkingSlot ps = new ParkingSlot();
            ps.setOccupied(false);
            parkingSlots.add(ps);
        }

        return parkingSlots;
    }
}
