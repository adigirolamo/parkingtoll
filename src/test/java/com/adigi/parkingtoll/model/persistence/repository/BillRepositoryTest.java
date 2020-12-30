package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.model.persistence.entity.BaseEntityTest;
import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.adigi.parkingtoll.model.enums.EngineType.ELECTRIC_50KW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BillRepositoryTest extends BaseEntityTest {

    @Autowired
    private BillRepository billRepository;

    private final String PARKING_NAME = "test";
    private final String PLATE = "AAa";

    @BeforeEach
    public void setUp() {
        parking = new Parking();

        parking.setNameUid(PARKING_NAME);
        parking.setPricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS);
        entityManager.persistAndFlush(parking);
    }

    @Test
    public void retrieveByParkingNamePlate_getBill() {

        // given
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, ELECTRIC_50KW).getReservation();
        r.setPlate(PLATE);

        // when
        entityManager.persistAndFlush(r);

        Bill billDb = billRepository.retrieveByParkingNameAndPlate(PARKING_NAME, r.getPlate());

        assertEquals(billDb, r.getBill());
    }

    @Test
    public void retrieveByParkingNamePlate_whenWrongParkingName_getNull() {

        // given
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, ELECTRIC_50KW).getReservation();
        r.setPlate(PLATE);

        // when
        entityManager.persistAndFlush(r);

        Bill billDb = billRepository.retrieveByParkingNameAndPlate("Wrong", r.getPlate());

        assertNull(billDb);
    }

    @Test
    public void retrieveByParkingNamePlate_whenWrongPlate_getNull() {

        // given
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, ELECTRIC_50KW).getReservation();
        r.setPlate(PLATE);

        // when
        entityManager.persistAndFlush(r);

        Bill billDb = billRepository.retrieveByParkingNameAndPlate(PARKING_NAME, "AAA");

        assertNull(billDb);
    }

    @Test
    public void retrieveByParkingNamePlate_usingDerivedQuery_getBill() {

        // given
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, ELECTRIC_50KW).getReservation();
        r.setPlate(PLATE);

        // when
        entityManager.persistAndFlush(r);

        Bill billDb = billRepository
                .findFirstByReservationParkingSlotParkingNameUidAndReservationPlate(PARKING_NAME, r.getPlate());

        assertEquals(billDb, r.getBill());
    }

    @Test
    public void retrieveByParkingNamePlate_whenWrongParkingName_usingDerivedQuery_getNull() {

        // given
        Reservation r = buildParkingSlotReservationBill(parking, "abc", 1, ELECTRIC_50KW).getReservation();
        r.setPlate(PLATE);

        // when
        entityManager.persistAndFlush(r);

        Bill billDb = billRepository.
                findFirstByReservationParkingSlotParkingNameUidAndReservationPlate("Wrong", r.getPlate());

        assertNull(billDb);
    }
}
