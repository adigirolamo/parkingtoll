package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.model.enums.Currency;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Bill {

    @Id
    @Column(name = "RESERVATION_ID")
    private long id;

    @NotNull(message = "Currency may not be null")
    private Currency currency;

    @Column(name = "AMOUNT", precision = 6, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal amount;

    @OneToOne(cascade = {CascadeType.ALL})
    @MapsId
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;

    public static class BillBuilder {

        /**
         * Create and return default bill instance
         * Default bill has currency set has Currency.EURO
         *
         * @return
         */
        public BillBuilder prepareDefault() {
            this.currency(Currency.EURO);
            return this;
        }
    }
}
