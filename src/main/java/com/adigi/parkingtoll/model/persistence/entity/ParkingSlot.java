package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.enums.VehicleType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Parking slot entity.
 * It represent a parking slot of a parking.
 * <ul>
 *     <li>A Parking slot has associated a reservation (One to One relation).</li>
 *     <li>Reservation has associated a bill (One to One relation).</li>
 * </ul>
 * Therefore there is a direct link between a parking slot, its reservation and its bill
 */
@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"PARKING_ID", "position"})
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARKINGSLOT_ID")
    private long id;

    /**
     * @see Parking
     */
    @NotNull(message = "Parking is mandatory")
    @ManyToOne
    @JoinColumn(name = "PARKING_ID")
    private Parking parking;

    /**
     * Parking slot position
     */
    @NotBlank(message = "Position is mandatory")
    @Column(name = "POSITION", length = 5, nullable = false)
    private String position;

    /**
     * In which floor is the parking slot
     */
    @NotNull(message = "Floor is mandatory")
    @Column(name = "FLOOR")
    private Integer floor;

    /**
     * Vehicle type that the parking slot is designed for.
     *
     * @see VehicleType
     */
    @NotNull(message = "VehicleType may not be null")
    private VehicleType vehicleType;

    /**
     * Engine type that the parking slot is designed for.
     * Ex. if a parking slot has engine type ELECTRIC_20KW, it could be reserved only for electric 20KW cars
     *
     * @see EngineType
     */
    @NotNull(message = "EngineType may not be null")
    private EngineType engineType;

    @Column(name = "RESERVED")
    @ColumnDefault("false")
    private boolean reserved;

    /**
     * @see Reservation
     */
    @OneToOne(mappedBy = "parkingSlot", cascade = {CascadeType.ALL})
    private Reservation reservation;

    /**
     * Current state of the parking slot
     *
     * @see ParkingSlotState
     */
    @Column(name = "PARKINGSLOT_STATE")
    @NotNull(message = "ParkingSlotState may not be null")
    private ParkingSlotState parkingSlotState;

}
