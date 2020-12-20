package com.adigi.parkingtoll.service;

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

    //TODO check if make it transactional avoiding ghot read
    public List<ParkingSlot> findByParkingNameUid(String parkingNameUid) {

        //Get parking slots from ParkingNameUI for engineType

        // if not parking slot return null
        // else
            // pick first parking slot
            // mark parking slot occupato
            // 

        return parkingSlotRepository.findByParkingNameUid(parkingNameUid);
    }
}
