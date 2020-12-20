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
     * Update the parking slot (if found) setting occupied true
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

        if (freeParkingSlots.isEmpty()) {
            return null;
        }

        return updateFirstParkingSlotAndReservation(freeParkingSlots, plate);
    }

    /**
     * get the parsking slot and it updates its status to not occupied. Updates the reservatio too
     *
     * @param parkingSlotId
     * @return parsking slot or null if the parsking slot is not found
     */
    public ParkingSlot updateParkingSlotToFree(Long parkingSlotId, String parkingNameUid) {

        ParkingSlot parkingSlot = parkingSlotRepository.findFirstByIdAndParkingNameUid(parkingSlotId, parkingNameUid);

        if (parkingSlot != null) {
            parkingSlot.setOccupied(false);
            reservationService.updateReservationDeparture(parkingSlot);
            parkingSlotRepository.save(parkingSlot);
        }

        return parkingSlot;
    }

    /**
     * get the first free parking slot, set it as occupied, and after it updates the reservation.
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
    ParkingSlot updateFirstParkingSlotAndReservation(List<ParkingSlot> freeParkingSlots, String plate) {

        ParkingSlot parkingSlot = freeParkingSlots.get(0);

        parkingSlot.setOccupied(true);

        reservationService.updateReservationForIncomingCar(parkingSlot, plate);

        parkingSlotRepository.save(parkingSlot);

        return parkingSlot;
    }

}
