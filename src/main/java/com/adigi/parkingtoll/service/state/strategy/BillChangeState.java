package com.adigi.parkingtoll.service.state.strategy;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;
import com.adigi.parkingtoll.model.persistence.entity.Bill;
import com.adigi.parkingtoll.model.persistence.entity.Parking;
import com.adigi.parkingtoll.service.PricingStrategyService;
import com.adigi.parkingtoll.service.state.StateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static com.adigi.parkingtoll.model.enums.ParkingSlotState.*;

@Service
public class BillChangeState extends AbstractChangeState<Bill> {

    private PricingStrategyService pricingStrategyService;

    @Autowired
    public BillChangeState(PricingStrategyService pricingStrategyService) {
        super();
        this.pricingStrategyService = pricingStrategyService;
    }

    @PostConstruct
    @Override
    public void init() {

        changeStrategies.put(RESERVED, this::resetBillAmount);

        changeStrategies.put(PAYING, this::payingStrategy);

        changeStrategies.put(PAYED, this::resetBillAmount);

    }

    @Override
    public ParkingSlotState getActualState(Bill bill) {
        return bill.getReservation().getParkingSlot().getParkingSlotState();
    }

    private Parking retrieveParking(Bill bill) {
        return bill.getReservation().getParkingSlot().getParking();
    }

    private void resetBillAmount(Bill bill, StateData data) {
        bill.setAmount(BigDecimal.ZERO);
    }

    private void payingStrategy(Bill bill, StateData data) {

        BigDecimal amount = pricingStrategyService.calculateAmount(
                retrieveParking(bill), bill.getReservation(), data.getLocalPaymentDateTime());

        bill.setAmount(amount);

    }
}
