package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingTest extends BaseEntityTest {

    @BeforeEach
    public void setUp() {
        init();
        parking.setNameUid(NAME_UID);
    }

    @Test
    public void shouldPersistPricingPolicyEnumConvertedValue() {

        // given
        PricingPolicy pricingPolicy = PricingPolicy.ONLY_HOURS;
        parking.setPricingPolicy(pricingPolicy);

        // when
        entityManager.persistAndFlush(parking);

        // then
        Parking persistedParking = entityManager.find(Parking.class, parking.getId());

        assertEquals(parking.getId(), persistedParking.getId());
        assertEquals(NAME_UID, persistedParking.getNameUid());
        assertEquals(pricingPolicy, persistedParking.getPricingPolicy());
    }

    @Test
    public void shouldFindParkingByPricingPolicy() {

        // given
        PricingPolicy pricingPolicy = PricingPolicy.INITIAL_FEE_PLUS_HOURS;
        parking.setPricingPolicy(pricingPolicy);
        entityManager.persistAndFlush(parking);

        String jpql =
                "select c from Parking c where c.pricingPolicy = com.adigi.parkingtoll.model.enums.PricingPolicy.INITIAL_FEE_PLUS_HOURS";

        // when
        List<Parking> parkings = entityManager.getEntityManager().createQuery(jpql, Parking.class).getResultList();

        // then
        assertEqualLastParkingFromList(parkings, pricingPolicy, NAME_UID);
    }

    @Test
    public void shouldFindParkingByEngineTypeParameter() {

        // given
        final String nameTest = "TEST_PARAM";
        PricingPolicy pricingPolicy = PricingPolicy.ONLY_HOURS;

        updateParking(parking, pricingPolicy, nameTest);
        entityManager.persistAndFlush(parking);

        // when
        List<Parking> parkings = retrieveParkingByPricingPolicyParameter(pricingPolicy);

        // then
        assertEqualLastParkingFromList(parkings, pricingPolicy, nameTest);
    }

    private void assertEqualLastParkingFromList(List<Parking> parkings, PricingPolicy pricingPolicy, String nameUid) {

        assertTrue(!parkings.isEmpty());
        assertEquals(pricingPolicy, parkings.get(parkings.size() - 1).getPricingPolicy());
        assertEquals(nameUid, parkings.get(parkings.size() - 1).getNameUid());

    }

}
