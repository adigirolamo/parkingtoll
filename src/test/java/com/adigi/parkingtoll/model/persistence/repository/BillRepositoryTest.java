package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import com.adigi.parkingtoll.test.annotation.DataJpaTestJunit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;
import java.util.function.BiConsumer;

import static com.adigi.parkingtoll.model.enums.EngineType.ELECTRIC_50KW;
import static com.adigi.parkingtoll.model.enums.ParkingSlotState.FREE;
import static com.adigi.parkingtoll.model.enums.PricingPolicy.ONLY_HOURS;
import static com.adigi.parkingtoll.model.enums.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTestJunit
public class BillRepositoryTest {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final String PARKING_NAME = "test";
    private final String PLATE = "AAa";

    //TODO refactor using builder
    @Test
    public void retrieveByParkingNamePlate_getBill() {

        // given
        ParkingSlot ps = new ParkingSlot();
        Bill bill = configureBill(ps);

        Bill billDb = billRepository.retrieveByParkingNameParkingSlotIdBillId(PARKING_NAME, bill.getReservation().getPlate());

        assertEquals(billDb, bill);
    }

    @Test
    public void retrieveByParkingNamePlate_whenWrongParkingName_getNull() {

        // given
        ParkingSlot ps = new ParkingSlot();
        Bill bill = configureBill(ps);

        Bill billDb = billRepository.retrieveByParkingNameParkingSlotIdBillId("Wrong", bill.getReservation().getPlate());

        assertNull(billDb);
    }

    @Test
    public void retrieveByParkingNamePlate_whenWrongPlate_getNull() {

        // given
        ParkingSlot ps = new ParkingSlot();
        Bill bill = configureBill(ps);

        Bill billDb = billRepository.retrieveByParkingNameParkingSlotIdBillId(PARKING_NAME, "AAA");

        assertNull(billDb);
    }

    //TODO refactor
    private Bill configureBill(ParkingSlot ps) {
        // given
        //TODO refactor using bi builder
        Parking p = Parking.builder().nameUid(PARKING_NAME).build();
        p.setPricingPolicy(ONLY_HOURS);
        //TODO user default Ps builder
        ps.setPosition("P");
        ps.setVehicleType(CAR);
        ps.setEngineType(ELECTRIC_50KW);
        ps.setFloor(2);
        ps.setParkingSlotState(FREE);
        Reservation r = Reservation.builder().plate(PLATE).build();
        Bill bill = Bill.builder().prepareDefault().build();

        //TODO change to
        //setEachOther( p, ps, r, bill);
        setEachOther(p, ps,
                (a, b) -> a.setParkingSlots(Set.of(b)),
                (b, a) -> b.setParking(a));
        setEachOther(ps, r,
                (a, b) -> a.setReservation(b),
                (b, a) -> b.setParkingSlot(a));
        setEachOther(bill, r,
                (a, b) -> a.setReservation(b),
                (b, a) -> b.setBill(a));

        entityManager.persist(p);
        entityManager.persist(ps);
        entityManager.persist(r);
        entityManager.persistAndFlush(bill);

        return bill;
    }

    private void setEachOther(Parking p, ParkingSlot ps, Reservation r, Bill bill) {
        setEachOther(p, ps,
                (a, b) -> a.setParkingSlots(Set.of(b)),
                (b, a) -> b.setParking(a));
        setEachOther(ps, r,
                (a, b) -> a.setReservation(b),
                (b, a) -> b.setParkingSlot(a));
        setEachOther(bill, r,
                (a, b) -> a.setReservation(b),
                (b, a) -> b.setBill(a));
    }

    private <T, U> void setEachOther(T t, U u, BiConsumer<T, U> saveFirst, BiConsumer<U, T> saveSecond) {

        saveFirst.accept(t, u);
        saveSecond.accept(u, t);

    }
}
