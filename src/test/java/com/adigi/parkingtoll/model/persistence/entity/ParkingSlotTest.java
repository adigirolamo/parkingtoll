package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingSlotTest extends BaseEntityTest {

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void insertNotUniqueParkingIdPosition_getViolation() {

        // given
        ParkingSlot parkingSlot1 = buildParkingSlot(parking, POSITION, FLOOR, EngineType.ELECTRIC_50KW);
        entityManager.persistAndFlush(parkingSlot1);
        ParkingSlot parkingSlot2 = buildParkingSlot(parking, POSITION, FLOOR, EngineType.ELECTRIC_50KW);

        // when
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(parkingSlot2);
        });

        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }

    @Test
    public void shouldPersistEngineTypeEnumConvertedValue() {

        // given
        EngineType engineType = EngineType.GASOLINE;
        ParkingSlot parkingSlot = buildParkingSlot(parking, POSITION, FLOOR, engineType);

        // when
        entityManager.persistAndFlush(parkingSlot);

        ParkingSlot persistedParkingSlot = entityManager.find(ParkingSlot.class, parkingSlot.getId());

        // then
        assertEquals(parkingSlot.getId(), persistedParkingSlot.getId());
        assertEquals(POSITION, persistedParkingSlot.getPosition());
        assertEquals(engineType, persistedParkingSlot.getEngineType());
    }

    @Test
    public void shouldFindParkingSlotByEngineType() {

        // given
        EngineType engineType = EngineType.ELECTRIC_20KW;
        ParkingSlot parkingSlot = buildParkingSlot(parking, POSITION, FLOOR, engineType);
        entityManager.persistAndFlush(parkingSlot);

        String jpql =
                "select c from ParkingSlot c "
                        + "where c.engineType = com.adigi.parkingtoll.model.enums.EngineType.ELECTRIC_20KW";

        // when
        List<ParkingSlot> parkingSlots = entityManager.getEntityManager().createQuery(jpql, ParkingSlot.class).getResultList();

        // then
        assertEqualLastParkingSlotFromList(parkingSlots, engineType, POSITION);
    }

    @Test
    public void shouldFindParkingSlotByEngineTypeParameter() {

        // given
        EngineType engineType = EngineType.ELECTRIC_50KW;

        ParkingSlot parkingSlot = buildParkingSlot(parking, POSITION, FLOOR, engineType);
        entityManager.persistAndFlush(parkingSlot);

        // when
        List<ParkingSlot> parkingSlotsDb = retrieveParkingSlotByEngineTypeParameter(engineType);

        // then
        assertEqualLastParkingSlotFromList(parkingSlotsDb, engineType, POSITION);
    }

}
