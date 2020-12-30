package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.PricingPolicy;
import com.adigi.parkingtoll.test.annotation.DataJpaTestJunit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.TypedQuery;
import java.util.List;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.FREE;
import static com.adigi.parkingtoll.model.enums.VehicleType.CAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestJunit
public class BaseEntityTest {

    public static final String NAME_UID = "NAME_UID";

    @Autowired
    protected TestEntityManager entityManager;

    protected Parking parking;

    protected final String POSITION = "POS1";
    protected final int FLOOR = 1;


    protected void init() {
        parking = new Parking();

        parking.setNameUid("PARKING_TEST");
        parking.setPricingPolicy(PricingPolicy.INITIAL_FEE_PLUS_HOURS);
        entityManager.persistAndFlush(parking);
    }


    protected List<ParkingSlot> retrieveParkingSlotByEngineTypeParameter(EngineType engineType) {

        String jpql = "select c from ParkingSlot c where c.engineType = :engineType";

        TypedQuery<ParkingSlot> query = entityManager.getEntityManager().createQuery(jpql, ParkingSlot.class);
        query.setParameter("engineType", engineType);

        return query.getResultList();
    }

    protected void assertEqualLastParkingSlotFromList(List<ParkingSlot> parkingSlots, EngineType engineType, String position) {

        assertTrue(!parkingSlots.isEmpty());
        assertEquals(engineType, parkingSlots.get(parkingSlots.size() - 1).getEngineType());
        assertEquals(position, parkingSlots.get(parkingSlots.size() - 1).getPosition());

    }

    protected ParkingSlot buildParkingSlot(Parking parking, String position, Integer floor, EngineType engineType) {

        ParkingSlot parkingSlot = new ParkingSlot();

        parkingSlot.setParking(parking);
        parkingSlot.setVehicleType(CAR);
        parkingSlot.setPosition(position);
        parkingSlot.setFloor(floor);
        parkingSlot.setEngineType(engineType);
        parkingSlot.setParkingSlotState(FREE);

        return parkingSlot;
    }

    protected Reservation configureReservation(ParkingSlot ps) {
        Reservation r = Reservation.builder().parkingSlot(ps).build();
        ps.setReservation(r);
        return r;
    }

    protected Bill configureBill(Reservation r) {
        Bill b = Bill.builder().prepareDefault().reservation(r).build();
        r.setBill(b);
        return b;
    }

    protected ParkingSlot buildParkingSlotReservationBill(Parking parking, String position, Integer floor, EngineType engineType) {

        ParkingSlot parkingSlot = buildParkingSlot(parking, position, floor, engineType);
        Reservation reservation = configureReservation(parkingSlot);
        configureBill(reservation);

        return parkingSlot;
    }

    protected List<Parking> retrieveParkingByPricingPolicyParameter(PricingPolicy pricingPolicy) {

        String jpql = "select c from Parking c where c.pricingPolicy = :pricingPolicy";

        TypedQuery<Parking> query = entityManager.getEntityManager().createQuery(jpql, Parking.class);
        query.setParameter("pricingPolicy", pricingPolicy);

        return query.getResultList();
    }

    protected void updateParking(Parking parking, PricingPolicy pricingPolicy, String nameUid) {

        if (pricingPolicy != null) {
            parking.setPricingPolicy(pricingPolicy);
        }

        if (nameUid != null) {
            parking.setNameUid(nameUid);
        }
    }
}
