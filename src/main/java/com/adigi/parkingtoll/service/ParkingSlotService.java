package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.repository.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private ReservationService reservationService;

    /**
     * retrieve free Parking slot of parking toll parkingNameUid, for a specified engine type
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
                parkingSlotRepository.findByParkingNameUidAndEngineTypeAndOccupiedFalse(parkingNameUid, engineType);

        //TODO minor importance test INTEGRATION check for empty result no exception
        if (freeParkingSlots.isEmpty()) {
            return null;
        }

        return updateFirstParkingSlotAndReservation(freeParkingSlots, plate);
    }

    ParkingSlot updateFirstParkingSlotAndReservation(List<ParkingSlot> freeParkingSlots, String plate) {

        ParkingSlot parkingSlot = freeParkingSlots.get(0);

        parkingSlot.setOccupied(true);

        reservationService.updateReservationPlateAndArrival(parkingSlot, plate);

        parkingSlotRepository.save(parkingSlot);

        return parkingSlot;
    }

}
