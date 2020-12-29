package com.adigi.parkingtoll.service.state;

import com.adigi.parkingtoll.exception.WrongStateException;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistance.entity.Bill;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.service.state.strategy.BillChangeState;
import com.adigi.parkingtoll.service.state.strategy.ParkingSlotChangeState;
import com.adigi.parkingtoll.service.state.strategy.ReservationChangeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;

@Service
public class ParkingSlotStateService {

    private BillChangeState billChangeState;
    private ParkingSlotChangeState parkingSlotChangeState;
    private ReservationChangeState reservationChangeState;

    private Map<ParkingSlotState, Set<ParkingSlotState>> allowedChanges = new HashMap<>();
    private String defaultMessage;

    @Autowired
    public ParkingSlotStateService(BillChangeState billChangeState,
                                   ParkingSlotChangeState parkingSlotChangeState,
                                   ReservationChangeState reservationChangeState) {
        this.billChangeState = billChangeState;
        this.parkingSlotChangeState = parkingSlotChangeState;
        this.reservationChangeState = reservationChangeState;
        this.allowedChanges = new TreeMap<>();
    }

    /**
     * Define all the allowed changes
     * key is the actual status
     * value is a list of accepted next state by the key state
     * <p>
     * Define default message
     */
    @PostConstruct
    public void init() {
        allowedChanges.put(FREE, Set.of(RESERVED));
        allowedChanges.put(RESERVED, Set.of(PAYING));
        allowedChanges.put(PAYING, Set.of(PAYING, PAYED));
        allowedChanges.put(PAYED, Set.of(FREE));

        initDefaultMessage();
    }

    public void changeState(ParkingSlot parkingSlot, ParkingSlotState nextState, StateData data) {
        ParkingSlotState actualState = parkingSlotChangeState.getActualState(parkingSlot);

        checkChange(actualState, nextState);

        parkingSlotChangeState.change(parkingSlot, nextState, data);
        reservationChangeState.change(parkingSlot.getReservation(), nextState, data);
        billChangeState.change(parkingSlot.getReservation().getBill(), nextState, data);
    }

    public void changeState(Bill bill, ParkingSlotState nextState, StateData data) {
        changeState(bill.getReservation().getParkingSlot(), nextState, data);
    }

    //

    void checkChange(ParkingSlotState from, ParkingSlotState to) {
        if (!isAllowed(from, to)) {
            throw new WrongStateException(exceptionMessage(from, to));
        }
    }

    String exceptionMessage(ParkingSlotState from, ParkingSlotState to) {
        return String.format(defaultMessage, from.toString(), to.toString());
    }

    private boolean isAllowed(ParkingSlotState from, ParkingSlotState to) {
        return allowedChanges.get(from).contains(to);
    }

    private void initDefaultMessage() {

        StringJoiner joiner = new StringJoiner("");

        joiner.add("Change state from %s to %s is not allowed. Supported state ");

        for (Map.Entry<ParkingSlotState, Set<ParkingSlotState>> entry : allowedChanges.entrySet()) {
//TODO refactor in single method below
            joiner.add("for [");
            joiner.add(entry.getKey().toString());

            joiner.add("] allowed [");
            String joinValues = entry.getValue().stream().map(Objects::toString).collect(Collectors.joining(" , "));

            joiner.add(joinValues);
            joiner.add("]; ");
        }

        defaultMessage = joiner.toString();
    }

}
