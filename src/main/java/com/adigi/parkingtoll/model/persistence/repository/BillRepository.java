package com.adigi.parkingtoll.model.persistence.repository;

import com.adigi.parkingtoll.model.persistence.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Long> {

    //TODO remove
    Bill findFirstByReservationParkingSlotIdAndReservationParkingSlotParkingNameUid(Long id, String parkingNameUid);

    Bill findFirstByReservationParkingSlotParkingNameUidAndReservationPlate(String parkingNameUid, String plate);

    /**
     * Return bill by parkingName and plate
     *
     * @param parkingNameUid parking name
     * @param plate          vehicle plate
     * @return Bill
     */
    @Query("select b from Bill b " +
            "inner join b.reservation res " +
            "inner join res.parkingSlot ps " +
            "where ps.parking.nameUid = :parkingName " +
            "and res.plate = :plate")
    Bill retrieveByParkingNameParkingSlotIdBillId(
            @Param("parkingName") String parkingNameUid,
            @Param("plate") String plate
    );
}
