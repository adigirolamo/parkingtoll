package com.adigi.parkingtoll.service;

import com.adigi.parkingtoll.model.persistence.repository.BillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BillServiceUnitTest {

    @Spy
    private ExceptionService ExceptionService;

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    @Test
    public void givenNullBill_whenCalculateBill_throwsEntityNotFoundException() throws Exception {

        // when
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            billService.calculateBillForLeavingCar(null, null);
        });

        assertTrue(exception.getMessage().contains("Not found Bill"));
    }

    @Test
    public void givenNullBill_whenPayBill_throwsEntityNotFoundException() throws Exception {

        // when
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            billService.payBill(null, null, null);
        });

        assertTrue(exception.getMessage().contains("Not found Bill"));
    }

}