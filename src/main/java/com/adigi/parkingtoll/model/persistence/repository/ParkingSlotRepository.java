package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByParkingNameUidAndEngineTypeAndReservedFalse(String parkingNameUid, EngineType engineType);

    ParkingSlot findFirstByIdAndParkingNameUid(Long id, String parkingNameUid);
}
