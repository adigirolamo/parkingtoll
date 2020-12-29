package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import com.adigi.parkingtoll.service.PricingStrategyService;
import com.adigi.parkingtoll.service.state.StateData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BillChangeState.class})
class BillChangeStateUnitTest {

    @MockBean
    private PricingStrategyService pricingStrategyService;

    @Autowired
    private BillChangeState billChangeState;

    @Test
    public void givenReservation_whenGetActualState_getCorrectState() {
        // given
        Bill bill = buildBill(PAYED);

        // when
        ParkingSlotState actualState = billChangeState.getActualState(bill);

        // then
        assertThat(actualState, equalTo(PAYED));
    }

    @Test
    public void givenBill_whenChangeRESERVED_getReservationCorrectFields() {

        // given
        Bill bill = Bill.builder().amount(BigDecimal.ONE).build();

        // when
        billChangeState.change(bill, RESERVED, StateData.builder().build());

        // then
        assertThat(bill.getAmount(), equalTo(BigDecimal.ZERO));
    }

    @Test
    public void givenBill_whenChangePAYING_getReservationCorrectFields() {

        // given
        Bill bill = buildBill(PAYED);
        bill.setAmount(BigDecimal.valueOf(5L));
        when(pricingStrategyService.calculateAmount(any(), any(), any())).thenReturn(BigDecimal.ZERO);

        // when
        billChangeState.change(bill, PAYING, StateData.builder().build());

        // then
        assertThat(bill.getAmount(), equalTo(BigDecimal.ZERO));
    }

    @Test
    public void givenBill_whenChangePAYED_getReservationCorrectFields() {

        // given
        Bill bill = Bill.builder().amount(BigDecimal.ONE).build();

        // when
        billChangeState.change(bill, PAYED, StateData.builder().build());

        // then
        assertThat(bill.getAmount(), equalTo(BigDecimal.ZERO));
    }

    private Bill buildBill(ParkingSlotState state) {

        ParkingSlot ps = ParkingSlot.builder().parkingSlotState(state).build();
        Reservation reservation = Reservation.builder().parkingSlot(ps).build();
        return Bill.builder().reservation(reservation).build();
    }
}