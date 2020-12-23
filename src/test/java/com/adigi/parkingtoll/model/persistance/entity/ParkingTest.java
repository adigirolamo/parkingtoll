package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.test.annotation.DataJpaTestJunit;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestJunit
public class ParkingTest {

    private static final String NAME_UID = "NAME_UID";

    @Autowired
    private TestEntityManager entityManager;

    private Parking parking;

    @BeforeEach
    public void setUp() {
        parking = new Parking();
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

    private List<Parking> retrieveParkingByPricingPolicyParameter(PricingPolicy pricingPolicy) {

        String jpql = "select c from Parking c where c.pricingPolicy = :pricingPolicy";

        TypedQuery<Parking> query = entityManager.getEntityManager().createQuery(jpql, Parking.class);
        query.setParameter("pricingPolicy", pricingPolicy);

        return query.getResultList();
    }

    private void updateParking(Parking parking, PricingPolicy pricingPolicy, String nameUid) {

        if (pricingPolicy != null) {
            parking.setPricingPolicy(pricingPolicy);
        }

        if (nameUid != null) {
            parking.setNameUid(nameUid);
        }
    }
}
