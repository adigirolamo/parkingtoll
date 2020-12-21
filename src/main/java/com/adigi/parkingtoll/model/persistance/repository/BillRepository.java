package com.adigi.parkingtoll.model.persistance.repository;

import com.adigi.parkingtoll.model.persistance.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, Long> {

    //TODO revert order
    Bill findFirstByReservationParkingSlotIdAndReservationParkingSlotParkingNameUid(Long id, String parkingNameUid);

    /**
     * Return bill by parkingName , pslotId and billId
     *
     * @param parkingNameUid
     * @param parkingSlotId
     * @param billId
     * @return
     */
    @Query("select b from Bill b " +
            "inner join b.reservation.parkingSlot ps " +
            "where ps.parking.nameUid = :parkingName " +
            "and ps.id = :pslotId " +
            "and b.id = :billId")
    Bill retrieveByParkingNameParkingSlotIdBillId(
            @Param("parkingName") String parkingNameUid,
            @Param("pslotId") Long parkingSlotId,
            @Param("billId") Long billId);
}
