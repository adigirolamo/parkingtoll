package com.adigi.parkingtoll.model.persistance.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Reservation {

    //TODO it can share the PK with Parking Slot
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private long id;

    @Column(name = "PLATE", length = 20, nullable = true, unique = false)
    private String plate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKINGSLOT_ID", unique = true)
    //TODO verify if unique is mandatory for 1to1 relation//, referencedColumnName = "RESERVATION_ID")
    private ParkingSlot parkingSlot;

    //TODO verificare correttezza
    // valutare se fare long
    @Column
    private java.time.LocalDateTime localArriveDateTime;

    @Column
    private java.time.LocalDateTime localDepartureDateTime;

    public Reservation() {
    }

    public Reservation(ParkingSlot parkingSlot, LocalDateTime localArriveDateTime) {
        this.parkingSlot = parkingSlot;
        this.localArriveDateTime = localArriveDateTime;
    }
}
