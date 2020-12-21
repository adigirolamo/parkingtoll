package com.adigi.parkingtoll.model.persistance.entity.builder;

import com.adigi.parkingtoll.model.enums.Currency;
import com.adigi.parkingtoll.model.persistance.entity.Bill;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class BillBuilder implements Supplier<Bill> {

    /**
     * create and return default bill instance
     *
     * @return Bill
     */
    @Override
    public Bill get() {
        Bill bill = new Bill();

        setDefaultCurrency(bill);

        return bill;
    }

    private void setDefaultCurrency(Bill bill) {
        bill.setCurrency(Currency.EURO);
    }
}
