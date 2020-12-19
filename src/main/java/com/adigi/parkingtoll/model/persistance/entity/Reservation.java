package com.adigi.parkingtoll.model.persistance.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CAR_ID") //, referencedColumnName = "RESERVATION_ID")
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKINGSLOT_ID") //, referencedColumnName = "RESERVATION_ID")
    private ParkingSlot parkingSlot;

    //TODO verificare correttezza
    // valutare se fare long
    @Column
    private java.time.LocalDateTime localArriveDateTime;

    @Column
    private java.time.LocalDateTime localDepartureDateTime;
}
