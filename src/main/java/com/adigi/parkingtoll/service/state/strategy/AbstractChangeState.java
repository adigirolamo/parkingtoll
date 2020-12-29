package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.service.state.StateData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class AbstractChangeState<T> implements ChangeState<T> {

    protected Map<ParkingSlotState, BiConsumer<T, StateData>> changeStrategies;

    public AbstractChangeState() {
        this.changeStrategies = new HashMap<>();
    }

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
