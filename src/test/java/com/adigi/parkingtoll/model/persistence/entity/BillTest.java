package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BillTest extends BaseEntityTest {

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    void givenParkingSlotReservationBill_whenPersistFlush_getSavedBill() {

        // given
        ParkingSlot parkingSlot = buildParkingSlotReservationBill(parking, POSITION, FLOOR, EngineType.ELECTRIC_50KW);

        Bill bill = entityManager.persistAndFlush(parkingSlot.getReservation().getBill());

        assertThat(bill.getId(), is(parkingSlot.getReservation().getBill().getId()));
    }

    @Test
    void givenBillWithoutPlate_whenPersistFlush_getConstraintViolationException() {

        // given
        ParkingSlot parkingSlot = buildParkingSlotReservationBill(parking, POSITION, FLOOR, EngineType.ELECTRIC_50KW);
        Bill bill = parkingSlot.getReservation().getBill();
        bill.setCurrency(null);

        // when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(bill);
        });

    }
}
