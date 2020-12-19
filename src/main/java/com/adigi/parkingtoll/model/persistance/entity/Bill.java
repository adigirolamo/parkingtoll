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

    @NotNull(message = "Currency may not be null")
    private Currency currency;

    @Column(name = "AMOUNT", precision = 6, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal amount;

    @Column
    @ColumnDefault("false")
    private Boolean active;

    @Column
    @ColumnDefault("false")
    private Boolean payed;

    @OneToOne
    @MapsId
    @JoinColumn(name = "PARKINGSLOT_ID")
    private ParkingSlot parkingSlot;
}
