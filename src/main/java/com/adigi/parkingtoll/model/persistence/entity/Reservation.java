package com.adigi.parkingtoll.model.persistence.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"PLATE"}, name = "UK_PLATE"),
                @UniqueConstraint(columnNames = {"PARKINGSLOT_ID"})
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private long id;

    /**
     * It is the plate of the vehicle that is using the parking slot related to this reservation.
     * It can be null if there is not any vehicle using the parking slot related to this reservation.
     */
    @Column(name = "PLATE", length = 20)
    private String plate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKINGSLOT_ID")
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

}
