package com.adigi.parkingtoll.model.persistence.entity;

import com.adigi.parkingtoll.model.enums.Currency;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Bill entity.
 * It represents a bill related to a Parking slot.
 * The relation chain is the following:
 * <ul>
 *     <li>Parking slot has one to one relation with reservation</li>
 *     <li>reservation has one to one relation with bill</li>
 * </ul>
 * Therefore when a vehicle is using a parking slot, bill will be related to that vehicle.
 */
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

    /**
     * Currency
     *
     * @see Currency
     */
    @NotNull(message = "Currency may not be null")
    private Currency currency;

    /**
     * Amount that has to be paid by the vehicle.
     * It could be 0.
     */
    @Column(name = "AMOUNT", precision = 6, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal amount;

    /**
     * @see Reservation
     */
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
