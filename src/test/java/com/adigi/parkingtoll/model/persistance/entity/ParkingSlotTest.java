package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.annotation.DataJpaTestJunit;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.enums.VehicleType;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private ParkingSlot buildParkingSlot(Parking parking, String position, Integer floor, EngineType engineType) {

        ParkingSlot parkingSlot = new ParkingSlot();

        parkingSlot.setParking(parking);
        parkingSlot.setVehicleType(VehicleType.CAR);
        parkingSlot.setPosition(position);
        parkingSlot.setFloor(floor);
        parkingSlot.setEngineType(engineType);

        return parkingSlot;
    }
}
