package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.persistence.entity.BaseEntityTest;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.adigi.parkingtoll.model.enums.EngineType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParkingSlotRepositoryTest extends BaseEntityTest {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    private final String PARKING_NAME = "test";
    private final String PLATE = "AAa";

    private Parking parking2;

    @BeforeEach
    public void setUp() {
        parking = new Parking();

        parking.setNameUid(PARKING_NAME);
        parking.setPricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS);
        entityManager.persistAndFlush(parking);

        parking2 = Parking.builder().nameUid("P2").pricingPolicy(PricingPolicy.ONLY_HOURS).build();
        entityManager.persistAndFlush(parking2);
    }

    @Test
    public void givenMultipleParkingSlots_whenFindByParkingAndEngineType_getCorrectNumberParkingSlots() {

        // given
        persistParskingSlots(3, parking, "abc", 1, ELECTRIC_50KW);
        persistBuildParkingSlot(parking2, "abc1", 1, ELECTRIC_50KW);
        persistBuildParkingSlot(parking, "abc3", 1, GASOLINE);
        persistBuildParkingSlot(parking, "abc4", 1, ELECTRIC_20KW);

        // when
        List<ParkingSlot> parkingSlotsE50W = parkingSlotRepository
                .findByParkingNameUidAndEngineTypeAndReservedFalse(PARKING_NAME, ELECTRIC_50KW);

        //then
        assertEquals(parkingSlotsE50W.size(), 3);
        assertEquals(parkingSlotsE50W.get(0).getEngineType(), ELECTRIC_50KW);
        assertEquals(parkingSlotsE50W.get(1).getParking().getNameUid(), PARKING_NAME);
    }

    @Test
    public void givenMultipleParkingSlots_whenfindFirstByIdAndParkingNameUid_getCorrectParkingSlots() {

        persistBuildParkingSlot(parking2, "abc1", 1, ELECTRIC_50KW);
        ParkingSlot ps = persistBuildParkingSlot(parking, "abc3", 1, GASOLINE);

        ParkingSlot psDb = parkingSlotRepository.findFirstByIdAndParkingNameUid(ps.getId(), PARKING_NAME);

        assertEquals(psDb, ps);
    }

    private void persistParskingSlots(int number, Parking parking, String position, Integer floor, EngineType engineType) {
        for (int i = 0; i < number; i++) {
            persistBuildParkingSlot(parking, position + i, floor, engineType);
        }
    }

    private ParkingSlot persistBuildParkingSlot(Parking parking, String position, Integer floor, EngineType engineType) {
        ParkingSlot parkingSlot = buildParkingSlot(parking, position, floor, engineType);
        entityManager.persistAndFlush(parkingSlot);
        return parkingSlot;
    }
}