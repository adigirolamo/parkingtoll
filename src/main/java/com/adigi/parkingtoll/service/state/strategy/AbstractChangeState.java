package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.service.state.StateData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class AbstractChangeState<T> implements ChangeState<T> {

    /**
     * Strategy for each parking slot state.
     * If a concrete implementation of this class does not require any action for a specific state, the concrete
     * class could not implement that strategy.
     * Ex. BillChangeState does not applies any change on Bill instance when the parking slot status changes to FREE,
     * therefore BillChangeState does not implement the strategy for FREE
     */
    protected Map<ParkingSlotState, BiConsumer<T, StateData>> changeStrategies;

    public AbstractChangeState() {
        this.changeStrategies = new HashMap<>();
    }

    /**
     * Applies the strategy related to the parking slot state, on entity t
     *
     * @param t     Entity
     * @param state Parking slot state
     * @param data  additional external data needed to apply the strategy
     */
    public void change(T t, ParkingSlotState state, StateData data) {

        BiConsumer<T, StateData> strategy = getStrategy(state);

        if (strategy != null) {
            strategy.accept(t, data);
        }
    }

    public abstract void init();

    protected BiConsumer<T, StateData> getStrategy(ParkingSlotState parkingSlotState) {
        return changeStrategies.get(parkingSlotState);
    }
}
