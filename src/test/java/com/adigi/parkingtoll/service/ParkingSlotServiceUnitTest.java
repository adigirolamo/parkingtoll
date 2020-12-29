package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.repository.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ParkingSlotServiceUnitTest {

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @Spy
    private ExceptionService ExceptionService;

    @InjectMocks
    private ParkingSlotService parkingSlotService;

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

}
