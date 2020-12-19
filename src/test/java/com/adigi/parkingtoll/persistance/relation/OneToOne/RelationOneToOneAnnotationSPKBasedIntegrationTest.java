package com.adigi.parkingtoll.persistance.relation.OneToOne;

import com.adigi.parkingtoll.annotation.DataJpaTestJunit;
import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.enums.VehicleType;
import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.Parking;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTestJunit
public class RelationOneToOneAnnotationSPKBasedIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenData_whenInsert_thenCreates1to1relationship() {
        // given
        ParkingSlot parkingSlot = new ParkingSlot();
        Bill bill = new Bill();

        // when
        update(parkingSlot, bill);

        entityManager.persistAndFlush(parkingSlot);

        ParkingSlot parkingSlotDb = entityManager.find(ParkingSlot.class, parkingSlot.getId());

        // then
        assert1to1InsertedData(parkingSlot, bill, parkingSlotDb);
    }

    private void assert1to1InsertedData(ParkingSlot parkingSlot, Bill bill, ParkingSlot parkingSlotDb) {

        assertParkingSlot(parkingSlot, parkingSlotDb);

        assertBill(bill, parkingSlotDb.getBill());

    }

    private void assertParkingSlot(ParkingSlot parkingSlot, ParkingSlot parkingSlotDb) {

        assertNotNull(parkingSlotDb);
        assertEquals(parkingSlot.getPosition(), parkingSlotDb.getPosition());
        assertEquals(parkingSlot.getEngineType(), parkingSlotDb.getEngineType());
    }

    private void assertBill(Bill bill, Bill billDb) {

        assertNotNull(billDb);
        assertEquals(bill.getId(), billDb.getId());
        assertEquals(bill.getCurrency(), billDb.getCurrency());
        assertEquals(bill.getAmount(), billDb.getAmount());
    }

    private void update(ParkingSlot parkingSlot, Bill bill) {

        update(parkingSlot);
        update(bill);

        parkingSlot.setBill(bill);

        bill.setParkingSlot(parkingSlot);
    }

    private void update(ParkingSlot parkingSlot) {

        parkingSlot.setParking(createParking());
        parkingSlot.setPosition("AAA");
        parkingSlot.setFloor(1);
        parkingSlot.setVehicleType(VehicleType.CAR);
        parkingSlot.setEngineType(EngineType.GASOLINE);

    }

    private void update(Bill bill) {

        bill.setCurrency(Currency.EURO);
        bill.setAmount(new BigDecimal(10.0));
    }

    private Parking createParking() {
        Parking parking = new Parking();
        parking.setNameUid("A");
        parking.setPricingPolicy(PricingPolicy.ONLY_HOURS);

        entityManager.persistAndFlush(parking);

        return parking;
    }
}
