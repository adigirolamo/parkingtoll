package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.enums.VehicleType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull(message = "Parking is mandatory")
    @ManyToOne
    @JoinColumn(name = "PARKING_ID", nullable = false)
    private Parking parking;

    @NotBlank(message = "Position is mandatory")
    @Column(name = "POSITION", length = 5, nullable = false)   //TODO uso tutte e due
    private String position;

    @NotNull(message = "Floor is mandatory")
    @Column(name = "FLOOR")
    private Integer floor;

    @NotNull(message = "VehicleType may not be null")
    private VehicleType vehicleType;

    @NotNull(message = "EngineType may not be null")
    private EngineType engineType;

    @Column(name = "RESERVED")
    @ColumnDefault("false")
    private boolean reserved;

    @OneToOne(mappedBy = "parkingSlot",cascade = {CascadeType.ALL})
    private Reservation reservation;

    @Column(name = "PARKINGSLOT_STATE")
    @NotNull(message = "ParkingSlotState may not be null")
    private ParkingSlotState parkingSlotState;

}