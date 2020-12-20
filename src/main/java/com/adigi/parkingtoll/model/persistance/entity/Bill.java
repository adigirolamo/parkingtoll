package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.model.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class Bill {

    @Id
    @Column(name = "PARKINGSLOT_ID")
    private long id;

    //TODO possible to remove not null, I've added a defaut value
    @NotNull(message = "Currency may not be null")
    private Currency currency = Currency.EURO;

    @Column(name = "AMOUNT", precision = 6, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal amount;

    //TODO remove and move it to reservation
    @Column
    @ColumnDefault("false")
    private Boolean active;

    //TODO remove and move it to reservation
    @Column
    @ColumnDefault("false")
    private Boolean payed;

    //TODO move teh relation to reservation
    @OneToOne
    @MapsId
    @JoinColumn(name = "PARKINGSLOT_ID")
    private ParkingSlot parkingSlot;
}
