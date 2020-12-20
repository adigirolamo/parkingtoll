package com.adigi.parkingtoll.model.persistance.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private long id;

    @Column(name = "PLATE", length = 20, nullable = true, unique = false)
    private String plate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKINGSLOT_ID", unique = true)
    private ParkingSlot parkingSlot;

    @Column
    private java.time.LocalDateTime localArriveDateTime;

    @Column
    private java.time.LocalDateTime localDepartureDateTime;

    @Column
    private java.time.LocalDateTime localPaymentDateTime;

    @Column
    @ColumnDefault("true")
    private Boolean payed;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Bill bill;

    public Reservation() {
    }

    public Reservation(ParkingSlot parkingSlot, LocalDateTime localArriveDateTime) {
        this.parkingSlot = parkingSlot;
        this.localArriveDateTime = localArriveDateTime;
    }
}
