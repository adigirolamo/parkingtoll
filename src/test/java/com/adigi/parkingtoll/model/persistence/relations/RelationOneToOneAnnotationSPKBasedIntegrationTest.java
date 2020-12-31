package com.adigi.parkingtoll.model.persistence.relations;

import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.BaseEntityTest;
import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RelationOneToOneAnnotationSPKBasedIntegrationTest extends BaseEntityTest {

    @BeforeEach
    public void setUp() {
        init();
    }

    @Test
    public void givenData_whenInsert_thenCreates1to1relationship() {

        // when
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, EngineType.ELECTRIC_50KW).getReservation();
        Bill b = r.getBill();
        update(r, b);

        entityManager.persistAndFlush(r);

        Reservation reservationDb = entityManager.find(Reservation.class, r.getId());

        // then
        assert1to1InsertedData(r, b, reservationDb);
    }

    private void assert1to1InsertedData(Reservation reservation, Bill bill, Reservation reservationDb) {

        assertReservation(reservation, reservationDb);

        assertBill(bill, reservationDb.getBill());

    }

    private void assertReservation(Reservation reservation, Reservation reservationDb) {

        assertNotNull(reservationDb);
        assertEquals(reservation.getPlate(), reservationDb.getPlate());
        assertEquals(reservation.getPaid(), reservationDb.getPaid());
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

    }

    private void update(Reservation reservation) {
        reservation.setPlate("AAA");
        reservation.setPaid(true);

    }

    private void update(Bill bill) {

        bill.setCurrency(Currency.EURO);
        bill.setAmount(new BigDecimal(10.0));
    }

}
