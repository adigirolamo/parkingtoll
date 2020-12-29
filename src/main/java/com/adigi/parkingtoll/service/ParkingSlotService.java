package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.repository.ParkingSlotRepository;
import com.adigi.parkingtoll.service.state.ParkingSlotStateService;
import com.adigi.parkingtoll.service.state.StateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.FREE;
import static com.adigi.parkingtoll.model.enums.ParkingSlotState.RESERVED;

@Service
@Transactional
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private ExceptionService exceptionService;

    @Autowired
    private ParkingSlotStateService parkingSlotStateService;

    /**
     * retrieve free Parking slot of parking toll parkingNameUid, for a specified engine type
     * Update the parking slot (if found) setting Reserved true
     *
     * @param parkingNameUid
     * @param plate
     * @param engineType
     * @return free parking slot or null if there isn't any free parking slot for that engine type in parking toll
     */
    //TODO check if make it transactional avoiding ghost read
    public ParkingSlot getFreeParkingSlotByParkingAndEngineType(
            String parkingNameUid, String plate, EngineType engineType) {

        List<ParkingSlot> freeParkingSlots =
                parkingSlotRepository.findByParkingNameUidAndEngineTypeAndReservedFalse(parkingNameUid, engineType);

        exceptionService.checkNotNull(freeParkingSlots,
                String.format("ParkingSlot of type [%s] and parking [%s]", engineType.toString(), parkingNameUid));

        return updateFirstParkingSlotForIncomingCar(freeParkingSlots, plate);
    }

    /**
     * get the parking slot and updates its status to not Reserved. Updates the reservation too
     *
     * <p>
     * Details on reservation update:
     * departure is set to now
     *
     * @param parkingSlotId
     * @return parking slot or null if the parking slot is not found
     */
    public ParkingSlot updateParkingSlotToFree(Long parkingSlotId, String parkingNameUid) {

        ParkingSlot parkingSlot = parkingSlotRepository.findFirstByIdAndParkingNameUid(parkingSlotId, parkingNameUid);

        exceptionService.checkNotNull(parkingSlot,
                String.format("ParkingSlot id[%d] parking [%s]", parkingSlotId, parkingNameUid));

        parkingSlotStateService.changeState(parkingSlot, FREE, StateData.builder().build());

        return parkingSlot;
    }

    /**
     * get the first free parking slot, set it as Reserved, and after it updates the reservation.
     * <p>
     * Details on reservation update:
     * plate is set to the car plate
     * payed is set to false
     * arrival is set to now
     * departure is set to null
     *
     * @param freeParkingSlots
     * @param plate
     * @return
     */
    ParkingSlot updateFirstParkingSlotForIncomingCar(List<ParkingSlot> freeParkingSlots, String plate) {

        ParkingSlot parkingSlot = freeParkingSlots.get(0);

        parkingSlotStateService.changeState(parkingSlot, RESERVED, StateData.builder().plate(plate).build());

        return parkingSlot;
    }

}
