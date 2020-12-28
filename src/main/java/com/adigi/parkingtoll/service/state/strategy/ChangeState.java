package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.service.state.StateData;

public interface ChangeState<T> {

    public void change(T t, ParkingSlotState state, StateData data);

    public ParkingSlotState getActualState(T t);
}
