package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.service.state.StateData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParkingSlotChangeState.class})
class ParkingSlotChangeStateUnitTest {

    @Autowired
    private ParkingSlotChangeState parkingSlotChangeState;

    @Test
    public void givenReservation_whenGetActualState_getCorrectState() {
        // given
        ParkingSlot parkingSlot = ParkingSlot.builder().parkingSlotState(PAYED).build();

        // when
        ParkingSlotState actualState = parkingSlotChangeState.getActualState(parkingSlot);

        // then
        assertThat(actualState, equalTo(PAYED));
    }

    @Test
    public void givenParkingSlot_whenChangeToFREE_getParkingSlotCorrectFields() {

        // given
        ParkingSlot parkingSlot = ParkingSlot.builder().reserved(true).parkingSlotState(PAYED).build();

        // when
        parkingSlotChangeState.change(parkingSlot, FREE, StateData.builder().build());

        // then
        assertThat(parkingSlot.isReserved(), is(false));
        assertThat(parkingSlot.getParkingSlotState(), equalTo(FREE));
    }

    @Test
    public void givenParkingSlot_whenChangeToRESERVED_getParkingSlotCorrectFields() {

        // given
        ParkingSlot parkingSlot = ParkingSlot.builder().reserved(false).build();

        // when
        parkingSlotChangeState.change(parkingSlot, RESERVED, StateData.builder().build());

        // then
        assertThat(parkingSlot.isReserved(), is(true));
        assertThat(parkingSlot.getParkingSlotState(), equalTo(RESERVED));
    }

    @Test
    public void givenParkingSlot_whenChangeToPAYING_getParkingSlotCorrectFields() {

        // given
        ParkingSlot parkingSlot = ParkingSlot.builder().parkingSlotState(PAYED).build();

        // when
        parkingSlotChangeState.change(parkingSlot, PAYING, StateData.builder().build());

        // then
        assertThat(parkingSlot.getParkingSlotState(), equalTo(PAYING));
    }

    @Test
    public void givenParkingSlot_whenChangeToPAYED_getParkingSlotCorrectFields() {

        // given
        ParkingSlot parkingSlot = ParkingSlot.builder().parkingSlotState(FREE).build();

        // when
        parkingSlotChangeState.change(parkingSlot, PAYED, StateData.builder().build());

        // then
        assertThat(parkingSlot.getParkingSlotState(), equalTo(PAYED));
    }
}