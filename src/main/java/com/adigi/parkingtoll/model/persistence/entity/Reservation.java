package com.adigi.parkingtoll.model.persistence.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Reservation entity.
 * It holds all the data related to a parking slot when a vehicle is using it.
 * <p>Reservation data</p>
 * <ul>
 *     <li>vehicle arrival time</li>
 *     <li>vehicle payment time</li>
 *     <li>vehicle departure time</li>
 *     <li>vehicle's plate</li>
 *     <li>vehicle's bill</li>
 * </ul>
 * <p>Note</p>
 * <ul>
 *     <li>A Parking slot has associated a reservation (One to One relation).</li>
 *     <li>Reservation has associated a bill (One to One relation).</li>
 * </ul>
 * Therefore there is a direct link between a parking slot, its reservation and its bill
 */
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

    /**
     * vehicle arrival time.
     * <p>
     * It can be null
     */
    @Column
    private java.time.LocalDateTime localArriveDateTime;

    /**
     * vehicle departure time
     * <p>
     * It can be null (ex. when the car leaves)
     */
    @Column
    private java.time.LocalDateTime localDepartureDateTime;

    /**
     * vehicle payment time
     * <p>
     * It can be null (ex. when the car leaves)
     */
    @Column
    private java.time.LocalDateTime localPaymentDateTime;

    @Column
    @ColumnDefault("true")
    private Boolean paid;

    /**
     * @see Bill
     */
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Bill bill;

}
