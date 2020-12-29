package com.adigi.parkingtoll.service.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class StateMessageServiceUnitTest {

    @InjectMocks
    private StateMessageService stateMessageService;

    private String defaultMessage;

    @BeforeEach
    public void setUp() {
        stateMessageService.init();
        defaultMessage = "%s's state is %s and it can't change to %s. Allowed state%s from %s: %s";
    }

    @Test
    public void givenFromFREEtoPAYEDallowedRESERVED_whenExceptionMessage_getCorrectMessage() {

        String exceptionMessage = stateMessageService.exceptionMessage(FREE, PAYED, Set.of(RESERVED));
        System.out.println(exceptionMessage);

        assertThat(exceptionMessage, containsString("Parking slot's state is free and it can't change to payed."));
        assertThat(exceptionMessage, containsString("Allowed state from free: reserved"));
    }

    @Test
    public void givenFromPAYINGtoRESERVEDallowedPAYINGPAYED_whenExceptionMessage_getCorrectMessage() {

        String exceptionMessage = stateMessageService.exceptionMessage(PAYING, RESERVED, Set.of(PAYING, PAYED));
        System.out.println(exceptionMessage);

        assertThat(exceptionMessage, containsString("Bill's state is paying and it can't change to reserved."));
        assertThat(exceptionMessage, containsString("Allowed states from paying:"));
        assertThat(exceptionMessage, containsString("payed"));
        assertThat(exceptionMessage, containsString("paying"));
    }
}