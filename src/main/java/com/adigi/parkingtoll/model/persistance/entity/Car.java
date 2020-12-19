package com.adigi.parkingtoll.model.persistance.entity;


import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAR_ID")
    private long id;

    @NotBlank(message = "Plate is mandatory")
    @Column(name = "PLATE", length = 20, nullable = false, unique = false)   //TODO uso tutte e due
    private String nameUid;

    @NotNull(message = "VehicleType may not be null")
    private VehicleType vehicleType;

    @NotNull(message = "EngineType may not be null")
    private EngineType engineType;

    @OneToOne(mappedBy = "car")
    private Reservation reservation;

}
