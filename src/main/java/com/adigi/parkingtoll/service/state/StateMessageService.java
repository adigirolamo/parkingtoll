package com.adigi.parkingtoll.service.state;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StateMessageService {

    private String defaultMessage;

    @PostConstruct
    void init() {
        defaultMessage = "%s's state is %s and it can't go to %s. Allowed state%s from %s: %s";
    }

    public String exceptionMessage(ParkingSlotState from, ParkingSlotState to,
                                   Set<ParkingSlotState> allowedStates) {

        String entityName = from.getAssociatedEntity();
        String actualState = from.toString().toLowerCase();
        String wrongState = to.toString().toLowerCase();
        String declination = getSingularOrPlural(allowedStates);
        String allowed = getAllowedStates(allowedStates);


        return String.format(defaultMessage,
                entityName, actualState, wrongState, declination, actualState, allowed);
    }

    private String getSingularOrPlural(Set<ParkingSlotState> allowedStates) {
        return allowedStates.size() > 1 ? "s" : "";
    }

    private String getAllowedStates(Set<ParkingSlotState> allowedStates) {

        return allowedStates.stream()
                .map(ParkingSlotState::getCode)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));
    }
}
