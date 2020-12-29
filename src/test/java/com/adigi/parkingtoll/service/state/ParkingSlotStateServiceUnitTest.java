package com.adigi.parkingtoll.service.state;

import com.adigi.parkingtoll.exception.WrongStateException;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.service.state.strategy.BillChangeState;
import com.adigi.parkingtoll.service.state.strategy.ParkingSlotChangeState;
import com.adigi.parkingtoll.service.state.strategy.ReservationChangeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParkingSlotStateService.class})
class ParkingSlotStateServiceUnitTest {

    @MockBean
    private BillChangeState billChangeState;
    @MockBean
    private ParkingSlotChangeState parkingSlotChangeState;
    @MockBean
    private ReservationChangeState reservationChangeState;
    @MockBean
    private StateMessageService stateMessageService;

    @Autowired
    private ParkingSlotStateService parkingSlotStateService;

    @BeforeEach
    public void setUP() {
        parkingSlotStateService.init();
    }

    @Test
    public void givenFromFREEtoWrongStates_whenCheckChange_throwsWrongStateException() {

        executeCheckChangeThrowException(FREE, FREE);
        executeCheckChangeThrowException(FREE, PAYING);
        executeCheckChangeThrowException(FREE, PAYED);
    }

    @Test
    public void givenFromFREEtoRESERVED_whenCheckChange_noExceptionThrow() {

        parkingSlotStateService.checkChange(FREE, RESERVED);
    }

    @Test
    public void givenFromRESERVEDtoWrongStates_whenCheckChange_throwsWrongStateException() {

        executeCheckChangeThrowException(RESERVED, FREE);
        executeCheckChangeThrowException(RESERVED, RESERVED);
        executeCheckChangeThrowException(RESERVED, PAYED);
    }

    @Test
    public void givenFromRESERVEDtoPAYING_whenCheckChange_noExceptionThrow() {

        parkingSlotStateService.checkChange(RESERVED, PAYING);
    }


    @Test
    public void givenFromPAYINGtoWrongStates_whenCheckChange_throwsWrongStateException() {

        executeCheckChangeThrowException(PAYING, FREE);
        executeCheckChangeThrowException(PAYING, RESERVED);
    }

    @Test
    public void givenFromPAYINGtoGoodStates_whenCheckChange_noExceptionThrow() {

        parkingSlotStateService.checkChange(PAYING, PAYING);
        parkingSlotStateService.checkChange(PAYING, PAYED);
    }

    @Test
    public void givenFromPAYEDtoWrongStates_whenCheckChange_throwsWrongStateException() {

        when(stateMessageService.exceptionMessage(any(), any(), any())).thenReturn("");

        executeCheckChangeThrowException(PAYED, PAYED);
        executeCheckChangeThrowException(PAYED, PAYING);
        executeCheckChangeThrowException(PAYED, RESERVED);
    }

    @Test
    public void givenFromPAYEDtoGoodStates_whenCheckChange_noExceptionThrow() {

        parkingSlotStateService.checkChange(PAYED, FREE);
    }

    //

    private void executeCheckChangeThrowException(ParkingSlotState from, ParkingSlotState to) {

        Exception exception = assertThrows(WrongStateException.class, () -> {
            parkingSlotStateService.checkChange(from, to);
        });
    }
}