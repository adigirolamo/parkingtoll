package com.adigi.parkingtoll.model.persistance.repository;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByParkingNameUid(String parkingNameUid);

    List<ParkingSlot> findByParkingNameUidAndEngineTypeAndOccupiedFalse(String parkingNameUid, EngineType engineType);
}
