package com.adigi.parkingtoll.service.state;

import com.adigi.parkingtoll.exception.WrongStateException;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.service.state.strategy.BillChangeState;
import com.adigi.parkingtoll.service.state.strategy.ParkingSlotChangeState;
import com.adigi.parkingtoll.service.state.strategy.ReservationChangeState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;

@Service
public class ParkingSlotStateService {

    private BillChangeState billChangeState;
    private ParkingSlotChangeState parkingSlotChangeState;
    private ReservationChangeState reservationChangeState;

    private StateMessageService stateMessageService;

    private Map<ParkingSlotState, Set<ParkingSlotState>> allowedChanges;

    @Autowired
    public ParkingSlotStateService(BillChangeState billChangeState, ParkingSlotChangeState parkingSlotChangeState, ReservationChangeState reservationChangeState, StateMessageService stateMessageService, Map<ParkingSlotState, Set<ParkingSlotState>> allowedChanges) {
        this.billChangeState = billChangeState;
        this.parkingSlotChangeState = parkingSlotChangeState;
        this.reservationChangeState = reservationChangeState;
        this.stateMessageService = stateMessageService;
        this.allowedChanges = allowedChanges;
    }

    /**
     * Define all the allowed changes
     * key is the actual status
     * value is a list of accepted next state by the key state
     */
    @PostConstruct
    public void init() {
        allowedChanges.put(FREE, Set.of(RESERVED));
        allowedChanges.put(RESERVED, Set.of(PAYING));
        allowedChanges.put(PAYING, Set.of(PAYING, PAYED));
        allowedChanges.put(PAYED, Set.of(FREE));

    }

    /**
     * Execute all the changes needed on ParkingSlot, Reservation and Bill, to go from the actual Parking slot's state
     * to next state.
     * It verifies that the state change is allowed, otherwise WrongStateException will be thrown
     *
     * @param parkingSlot Parking slot
     * @param nextState   next state
     * @param data        additional external data needed to perform the state change
     */
    public void changeState(ParkingSlot parkingSlot, ParkingSlotState nextState, StateData data) {
        ParkingSlotState actualState = parkingSlotChangeState.getActualState(parkingSlot);

        checkChange(actualState, nextState);

        parkingSlotChangeState.change(parkingSlot, nextState, data);
        reservationChangeState.change(parkingSlot.getReservation(), nextState, data);
        billChangeState.change(parkingSlot.getReservation().getBill(), nextState, data);
    }

    /**
     * Execute all the changes needed on ParkingSlot, Reservation and Bill, to go from the actual Parking slot's state
     * to next state.
     * It verifies that the state change is allowed, otherwise WrongStateException will be thrown
     *
     * @param bill      Bill
     * @param nextState next state
     * @param data      additional external data needed to perform the state change
     */
    public void changeState(Bill bill, ParkingSlotState nextState, StateData data) {
        changeState(bill.getReservation().getParkingSlot(), nextState, data);
    }

    //

    /**
     * Verify that the state change is allowed, otherwise it throws WrongStateException
     *
     * @param from actual state
     * @param to   next state
     */
    void checkChange(ParkingSlotState from, ParkingSlotState to) {
        if (!isAllowed(from, to)) {
            throw new WrongStateException(
                    stateMessageService.exceptionMessage(from, to, getAllowedStates(from)));
        }
    }

    private boolean isAllowed(ParkingSlotState from, ParkingSlotState to) {
        return allowedChanges.get(from).contains(to);
    }

    private Set<ParkingSlotState> getAllowedStates(ParkingSlotState from) {
        return allowedChanges.get(from);
    }

}
