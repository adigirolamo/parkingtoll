package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.repository.ParkingSlotRepository;
import com.adigi.parkingtoll.service.state.ParkingSlotStateService;
import com.adigi.parkingtoll.service.state.StateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.FREE;
import static com.adigi.parkingtoll.model.enums.ParkingSlotState.RESERVED;

@Service
@Transactional
public class ParkingSlotService extends BaseControllerService {

    private ParkingSlotRepository parkingSlotRepository;


    @Autowired
    public ParkingSlotService(ParkingSlotRepository parkingSlotRepository, ExceptionService exceptionService, ParkingSlotStateService parkingSlotStateService) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.exceptionService = exceptionService;
        this.parkingSlotStateService = parkingSlotStateService;
    }

    /**
     * retrieve the first free Parking slot of the parking toll parkingNameUid.
     * The parking slot that is returned is for a specified engine type
     * It also updates the parking slot state to RESERVED
     *
     * @param parkingNameUid
     * @param plate
     * @param engineType     vehicle's engine type.
     * @return first free parking slot in the parking toll, for that vehicle's engine type
     * @see ParkingSlotStateService check status changes Business Logic
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ParkingSlot getFreeParkingSlotByParkingAndEngineType(
            String parkingNameUid, String plate, EngineType engineType) {

        List<ParkingSlot> freeParkingSlots =
                parkingSlotRepository.findByParkingNameUidAndEngineTypeAndReservedFalse(parkingNameUid, engineType);

        exceptionService.checkNotNull(freeParkingSlots,
                String.format("ParkingSlot of type [%s] and parking [%s]", engineType.toString(), parkingNameUid));

        return updateFirstParkingSlotForIncomingCar(freeParkingSlots, plate);
    }

    /**
     * Get the parking slot by its ID and updates its status to FREE.
     *
     * @param parkingSlotId parking slot id
     * @return parking slot after its update
     * @see ParkingSlotStateService check status changes Business Logic
     */
    public ParkingSlot updateParkingSlotToFree(Long parkingSlotId, String parkingNameUid) {

        ParkingSlot parkingSlot = parkingSlotRepository.findFirstByIdAndParkingNameUid(parkingSlotId, parkingNameUid);

        exceptionService.checkNotNull(parkingSlot,
                String.format("ParkingSlot id[%d] parking [%s]", parkingSlotId, parkingNameUid));

        parkingSlotStateService.changeState(parkingSlot, FREE, StateData.builder().build());

        return parkingSlot;
    }

    /**
     * Get the first parking slot in the list and changes its status to RESERVED
     *
     * @param freeParkingSlots
     * @param plate
     * @return
     * @see ParkingSlotStateService check status changes Business Logic
     */
    ParkingSlot updateFirstParkingSlotForIncomingCar(List<ParkingSlot> freeParkingSlots, String plate) {

        ParkingSlot parkingSlot = freeParkingSlots.get(0);

        parkingSlotStateService.changeState(parkingSlot, RESERVED, StateData.builder().plate(plate).build());

        return parkingSlot;
    }

}
