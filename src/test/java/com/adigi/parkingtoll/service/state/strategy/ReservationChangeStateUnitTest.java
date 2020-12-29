package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import com.adigi.parkingtoll.service.LocalDateTimeService;
import com.adigi.parkingtoll.service.state.StateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReservationChangeState.class})
class ReservationChangeStateUnitTest {

    @MockBean
    private LocalDateTimeService timeService;

    @Autowired
    private ReservationChangeState reservationChangeState;

    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        now = LocalDateTime.now();
    }

    @Test
    public void givenReservation_whenGetActualState_getCorrectState() {
        // given
        ParkingSlot ps = ParkingSlot.builder().parkingSlotState(PAYED).build();
        Reservation reservation = Reservation.builder().parkingSlot(ps).build();

        // when
        ParkingSlotState actualState = reservationChangeState.getActualState(reservation);

        // then
        assertThat(actualState, equalTo(PAYED));
    }

    @Test
    public void givenRservation_whenChangeFREE_getReservationCorrectFields() {
        Reservation reservation = Reservation.builder().plate("AA").build();
        when(timeService.getNow()).thenReturn(now);

        reservationChangeState.change(reservation, FREE, StateData.builder().build());

        // then
        assertThat(reservation.getPlate(), nullValue());
        assertThat(reservation.getLocalDepartureDateTime(), equalTo(now));
    }

    @Test
    public void givenRservation_whenChangeRESERVED_getReservationCorrectFields() {

        // given
        final String newPlate = "BB";
        Reservation reservation = Reservation.builder()
                .plate("AA").localDepartureDateTime(now).localPaymentDateTime(now).build();
        when(timeService.getNow()).thenReturn(now);

        // then
        reservationChangeState.change(reservation, RESERVED, StateData.builder().plate(newPlate).build());

        // then
        assertThat(reservation.getPlate(), equalTo(newPlate));
        assertThat(reservation.getPayed(), is(false));
        assertThat(reservation.getLocalArriveDateTime(), equalTo(now));
        assertThat(reservation.getLocalDepartureDateTime(), nullValue());
        assertThat(reservation.getLocalPaymentDateTime(), nullValue());
    }

    @Test
    public void givenRservation_whenChangePAYING_getReservationCorrectFields() {

        // given
        Reservation reservation = Reservation.builder().localPaymentDateTime(null).build();

        // when
        reservationChangeState.change(reservation, PAYING, StateData.builder().localPaymentDateTime(now).build());

        // then
        assertThat(reservation.getLocalPaymentDateTime(), equalTo(now));
    }

    @Test
    public void givenRservation_whenChangePAYED_getReservationCorrectFields() {

        // given
        Reservation reservation = Reservation.builder().payed(false).build();

        // when
        reservationChangeState.change(reservation, PAYED, StateData.builder().build());

        // then
        assertThat(reservation.getPayed(), is(true));
    }
}