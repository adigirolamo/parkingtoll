package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Long> {

    /**
     * Example of derived query to find bill from parking name UID and plate
     *
     * @param parkingNameUid parking unique name
     * @param plate          vehicle's plate
     * @return Bill
     */
    Bill findFirstByReservationParkingSlotParkingNameUidAndReservationPlate(String parkingNameUid, String plate);

    /**
     * Example of JPQL to find bill from parking name UID and plate
     *
     * @param parkingNameUid parking unique name
     * @param plate          vehicle plate
     * @return Bill
     */
    @Query("select b from Bill b " +
            "inner join b.reservation res " +
            "inner join res.parkingSlot ps " +
            "where ps.parking.nameUid = :parkingName " +
            "and res.plate = :plate")
    Bill retrieveByParkingNameAndPlate(
            @Param("parkingName") String parkingNameUid,
            @Param("plate") String plate
    );
}
