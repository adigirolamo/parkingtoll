package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
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

    @Spy
    private ExceptionService ExceptionService;

    @Mock
    private BillService billService;

    @InjectMocks
    private ParkingSlotService parkingSlotService;

    /**
     * Test method getFreeParkingSlotByParkingAndEngineType when the list - that is returned my ParkingSlotRepository -
     * is populated and verify that the returned PS is updated as Reserved
     */
    @Test
    public void getFreeParkingSlotByParkingAndEngineType_getUpdatedParkingSlot() {

        // given
        List<ParkingSlot> parkingSlots = new ArrayList<>(Arrays.asList(new ParkingSlot()));

        // when
        Mockito.when(parkingSlotRepository
                .findByParkingNameUidAndEngineTypeAndReservedFalse("ParkingUI", EngineType.GASOLINE))
                .thenReturn(parkingSlots);

        // method to verify
        ParkingSlot retrievedParkingSlot = parkingSlotService.getFreeParkingSlotByParkingAndEngineType(
                "ParkingUI", "PLATE", EngineType.GASOLINE);

        // verify
        assertTrue(retrievedParkingSlot.isReserved());
    }

    @Test
    public void givenEmptyList_whenGetFreeParkingSlotByParkingAndEngineType_throwEntityNotFoundException() {

        // given
        Mockito.when(parkingSlotRepository.
                findByParkingNameUidAndEngineTypeAndReservedFalse("ParkingUI", EngineType.GASOLINE))
                .thenReturn(Collections.emptyList());

        // when
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            parkingSlotService.getFreeParkingSlotByParkingAndEngineType(
                    "ParkingUI", "PLATE", EngineType.GASOLINE);
        });

        assertTrue(exception.getMessage().contains("Not found ParkingSlot"));

    }

    @Test
    public void updateFirstParkingSlotAndReservation_onlyFirstIsReserved() {

        // given
        List<ParkingSlot> slots = createNotReservedParkingSlots(3);


        // method to verify
        parkingSlotService.updateFirstParkingSlotForIncomingCar(slots, "TARGA");

        // verify
        assertTrue(slots.get(0).isReserved());
        assertFalse(slots.get(1).isReserved());
    }

    //TODO updateParkingSlotToFree
    // if parkingSlot returned
    // setReserved == false
    // Check if update the test and control reservation too

    //TODO updateParkingSlotToFree
    // if not returned no error

    private List<ParkingSlot> createNotReservedParkingSlots(int num) {

        List<ParkingSlot> parkingSlots = new ArrayList<>(num);

        for (int i = 0; i < num; i++) {
            ParkingSlot ps = new ParkingSlot();
            ps.setReserved(false);
            parkingSlots.add(ps);
        }

        return parkingSlots;
    }
}
