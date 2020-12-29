package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.service.LocalDateTimeService;
import com.adigi.parkingtoll.service.state.StateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;

@Service
public class ReservationChangeState extends AbstractChangeState<Reservation> {

    private final LocalDateTimeService timeService;

    @Autowired
    public ReservationChangeState(LocalDateTimeService timeService) {
        this.timeService = timeService;
    }

    @PostConstruct
    @Override
    public void init() {
        changeStrategies.put(FREE, this::freeStrategy);

        changeStrategies.put(RESERVED, this::reservedStrategy);

        changeStrategies.put(PAYING, (s, d) -> s.setLocalPaymentDateTime(d.getLocalPaymentDateTime()));

        changeStrategies.put(PAYED, (s, d) -> s.setPayed(true));
    }

    @Override
    public ParkingSlotState getActualState(Reservation reservation) {
        return reservation.getParkingSlot().getParkingSlotState();
    }

    private void freeStrategy(Reservation reservation, StateData data) {
        reservation.setLocalDepartureDateTime(timeService.getNow());
        reservation.setPlate(null);
    }

    private void reservedStrategy(Reservation reservation, StateData data) {

        reservation.setPlate(data.getPlate());
        reservation.setPayed(false);
        reservation.setLocalArriveDateTime(timeService.getNow());
        reservation.setLocalDepartureDateTime(null);
        reservation.setLocalPaymentDateTime(null);

    }
}
