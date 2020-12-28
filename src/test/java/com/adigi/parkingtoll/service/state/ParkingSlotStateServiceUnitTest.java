package com.adigi.parkingtoll.service.state;

import com.adigi.parkingtoll.exception.WrongStateException;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ParkingSlotStateServiceUnitTest {

    @InjectMocks
    private ParkingSlotStateService parkingSlotStateService;

    @BeforeEach
    public void setUP() {
        parkingSlotStateService.init();
    }

    @Test
    public void givenFromFREEtoFREE_whenExceptionMessage_getCorrectMessage() {

        String exceptionMessage = parkingSlotStateService.exceptionMessage(FREE, FREE);

        assertThat(exceptionMessage, containsString("is not allowed. Supported state for"));
        assertThat(exceptionMessage, containsString("Change state from FREE to FREE is not allowed"));
        assertThat(exceptionMessage, containsString("for [PAYED] allowed [FREE];"));
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

        // when
        Exception exception = assertThrows(WrongStateException.class, () -> {
            parkingSlotStateService.checkChange(from, to);
        });

        String expectedMsg = String.format("Change state from %s to %s is not allowed", from.toString(), to.toString());

        // then
        assertThat(exception.getMessage(), containsString(expectedMsg));
    }
}