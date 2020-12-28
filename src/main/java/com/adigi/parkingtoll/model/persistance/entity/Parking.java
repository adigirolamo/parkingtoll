package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.model.enums.PricingPolicy;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARKING_ID")
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "NAME_UID", length = 50, nullable = false, unique = true)
    private String nameUid;

    @Column(name = "DESCRIPTION", length = 100, nullable = true, unique = false)
    private String description;

    @Column(name = "FIXED_AMOUNT", precision = 4, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "MINUTE_PRICE", precision = 4, scale = 2)
    private BigDecimal minutePrice;

    @OneToMany(mappedBy = "parking",cascade = {CascadeType.ALL})
    private Set<ParkingSlot> parkingSlots;

    @NotNull(message = "PricingPolicy may not be null")
    private PricingPolicy pricingPolicy;

}
