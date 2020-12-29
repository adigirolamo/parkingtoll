package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;

@Service
public class ParkingSlotChangeState extends AbstractChangeState<ParkingSlot> {

    public ParkingSlotChangeState() {
    }

    @PostConstruct
    @Override
    public void init() {

        changeStrategies.put(FREE, (s, d) -> {
            s.setReserved(false);
            s.setParkingSlotState(FREE);
        });

        changeStrategies.put(RESERVED, (s, d) -> {
            s.setReserved(true);
            s.setParkingSlotState(RESERVED);
        });

        changeStrategies.put(PAYING, (s, d) -> s.setParkingSlotState(PAYING));

        changeStrategies.put(PAYED, (s, d) -> s.setParkingSlotState(PAYED));

    }

    @Override
    public ParkingSlotState getActualState(ParkingSlot parkingSlot) {
        return parkingSlot.getParkingSlotState();
    }
}
