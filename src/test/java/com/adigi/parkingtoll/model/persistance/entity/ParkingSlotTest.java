package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.test.annotation.DataJpaTestJunit;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.FREE;
import static com.adigi.parkingtoll.model.enums.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTestJunit
public class ParkingSlotTest {

    @Autowired
    private TestEntityManager entityManager;

    private Parking parking;

    private final String POSITION = "POS1";
    private final int FLOOR = 1;

    @BeforeEach
    public void setUp() {
        parking = new Parking();
        parking.setNameUid("PARKING_TEST");
        parking.setPricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS);
        entityManager.persistAndFlush(parking);
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

    private List<ParkingSlot> retrieveParkingSlotByEngineTypeParameter(EngineType engineType) {

        String jpql = "select c from ParkingSlot c where c.engineType = :engineType";

        TypedQuery<ParkingSlot> query = entityManager.getEntityManager().createQuery(jpql, ParkingSlot.class);
        query.setParameter("engineType", engineType);

        return query.getResultList();
    }

    private void assertEqualLastParkingSlotFromList(List<ParkingSlot> parkingSlots, EngineType engineType, String position) {

        assertTrue(!parkingSlots.isEmpty());
        assertEquals(engineType, parkingSlots.get(parkingSlots.size() - 1).getEngineType());
        assertEquals(position, parkingSlots.get(parkingSlots.size() - 1).getPosition());

    }

    private ParkingSlot buildParkingSlot(Parking parking, String position, Integer floor, EngineType engineType) {

        ParkingSlot parkingSlot = new ParkingSlot();

        parkingSlot.setParking(parking);
        parkingSlot.setVehicleType(CAR);
        parkingSlot.setPosition(position);
        parkingSlot.setFloor(floor);
        parkingSlot.setEngineType(engineType);
        parkingSlot.setParkingSlotState(FREE);

        return parkingSlot;
    }
}
