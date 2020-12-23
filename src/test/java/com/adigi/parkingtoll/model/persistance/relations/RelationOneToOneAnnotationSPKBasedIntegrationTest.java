package com.adigi.parkingtoll.model.persistance.relations;

import com.adigi.parkingtoll.test.annotation.DataJpaTestJunit;
import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
        Reservation reservation = new Reservation();
        Bill bill = Bill.builder().build();

        // when
        update(reservation, bill);

        entityManager.persistAndFlush(reservation);

        Reservation reservationDb = entityManager.find(Reservation.class, reservation.getId());

        // then
        assert1to1InsertedData(reservation, bill, reservationDb);
    }

    private void assert1to1InsertedData(Reservation reservation, Bill bill, Reservation reservationDb) {

        assertReservation(reservation, reservationDb);

        assertBill(bill, reservationDb.getBill());

    }

    private void assertReservation(Reservation reservation, Reservation reservationDb) {

        assertNotNull(reservationDb);
        assertEquals(reservation.getPlate(), reservationDb.getPlate());
        assertEquals(reservation.getPayed(), reservationDb.getPayed());
    }

    private void assertBill(Bill bill, Bill billDb) {

        assertNotNull(billDb);
        assertEquals(bill.getId(), billDb.getId());
        assertEquals(bill.getCurrency(), billDb.getCurrency());
        assertEquals(bill.getAmount(), billDb.getAmount());
    }

    private void update(Reservation reservation, Bill bill) {

        update(reservation);
        update(bill);

        reservation.setBill(bill);

        bill.setReservation(reservation);
    }

    private void update(Reservation reservation) {
        reservation.setPlate("AAA");
        reservation.setPayed(true);
//        reservation.setParking(createParking());
//        reservation.setPosition("AAA");
//        reservation.setFloor(1);
//        reservation.setVehicleType(VehicleType.CAR);
//        reservation.setEngineType(EngineType.GASOLINE);

    }

    private void update(Bill bill) {

        bill.setCurrency(Currency.EURO);
        bill.setAmount(new BigDecimal(10.0));
    }

//    private Parking createParking() {
//        Parking parking = new Parking();
//        parking.setNameUid("A");
//        parking.setPricingPolicy(PricingPolicy.ONLY_HOURS);
//
//        entityManager.persistAndFlush(parking);
//
//        return parking;
//    }
}
