package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistence.entity.Reservation;
import com.adigi.parkingtoll.model.persistence.repository.BillRepository;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.service.LocalDateTimeService;
import com.adigi.parkingtoll.test.helper.ReqPerformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.NAME_PARKING2;
import static org.junit.jupiter.api.Assertions.*;

public class BillControllerIntegrationTest extends ReqPerformer {

    @Autowired
    private BillRepository billRepository;

    @SpyBean
    private LocalDateTimeService timeService;

    private LocalDateTime paymentTimeMock;
    private String parking;

    @BeforeEach
    public void setUp() {
        init();

        paymentTimeMock = LocalDateTime.now().plusHours(1).minusMinutes(23);
        parking = NAME_PARKING2;
    }


    @Test
    public void givenWac_whenServletContext_thenItProvidesBillController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("billController"));
    }

    /**
     * Checks that when it is calculated the bill it is returned the correct bill:
     * <ul>
     *     <li>BillDTO amount is not null</li>
     *     <li>Reservation payment time is the same payment time</li>
     *     <li>Reservation paid is still false</li>
     *     <li>Parking Slot id, retrieved from bill, is the same as the parking slot returned from GetParkingSlot</li>
     * </ul>
     * <p>
     *
     * @throws Exception
     */
    @Test
    public void givenParkingAndPlate_whenCalculateBill_getCorrectBill() throws Exception {

        // given
        String plate = "plate";
        helper.addPlate(plate).addEngineType(EngineType.ELECTRIC_20KW.toString());
        Mockito.when(timeService.getNow()).thenReturn(paymentTimeMock);

        // when
        ParkingSlot parkingSlot = performGetParkingSlotGetParkingSlot(parking);
        ResultActions resultCalculatedBill = performGetCalculateBill(parking, plate);

        // then
        BillDTO billDTO = verifyResCalculatedBill(resultCalculatedBill);
        verifyEntitiesForCalculateBillWhenLeavingCar(parkingSlot.getId(), billDTO.getId(), paymentTimeMock);
    }

    /**
     * Checks that when it pays the bill it is returned the correct bill:
     * <ul>
     *     <li>BillDTO amount is null</li>
     *     <li>Reservation paid is true</li>
     * </ul>
     * <p>
     *
     * @throws Exception
     */
    @Test
    public void givenParkingPlate_whenPayBill_getCorrectBill() throws Exception {

        // given
        String plate = "plate";
        helper.addPlate(plate).addEngineType(EngineType.ELECTRIC_20KW.toString());
        Mockito.when(timeService.getNow()).thenReturn(paymentTimeMock);

        // when
        ParkingSlot parkingSlot = performGetParkingSlotGetParkingSlot(parking);
        BillDTO calculatedBillDto = mapper.getBillDTO(performGetCalculateBill(parking, plate));
        ResultActions paidBill = performPayBill(parking, plate);

        // then
        BillDTO paidBillDto = verifyResPayBill(paidBill, calculatedBillDto);
        verifyEntitiesForPayBillWhenLeavingCar(parkingSlot.getId(), paidBillDto.getId());
    }

    private ParkingSlot performGetParkingSlotGetParkingSlot(String parking) throws Exception {

        MvcResult resultGetParkingSlot = performGetParkingSlot(helper.getRequestParams(), parking).andReturn();

        return mapper.getResponseObject(resultGetParkingSlot, ParkingSlot.class);
    }

    private void verifyEntitiesForCalculateBillWhenLeavingCar(Long parkingSlotId, Long billId, LocalDateTime paymentTimeMock) {

        Reservation reservation = billRepository.findById(billId).get().getReservation();

        assertEquals(reservation.getParkingSlot().getId(), parkingSlotId);
        assertEquals(reservation.getLocalPaymentDateTime(), paymentTimeMock);
        assertFalse(reservation.getPaid());
    }

    private void verifyEntitiesForPayBillWhenLeavingCar(Long parkingSlotId, Long billId) {

        Reservation reservation = billRepository.findById(billId).get().getReservation();

        assertEquals(reservation.getParkingSlot().getId(), parkingSlotId);
        assertTrue(reservation.getPaid());
    }


    private BillDTO verifyResCalculatedBill(ResultActions resultBill) throws Exception {

        verifyOk(resultBill);

        BillDTO bill = mapper.getResponseObject(resultBill.andReturn(), BillDTO.class);

        assertNotEquals(bill.getAmount().compareTo(BigDecimal.ZERO), 0);

        return bill;
    }

    private BillDTO verifyResPayBill(ResultActions paidBill, BillDTO calculatedBillDto) throws Exception {

        verifyOk(paidBill);

        BillDTO paidBillDto = mapper.getResponseObject(paidBill.andReturn(), BillDTO.class);

        assertEquals(paidBillDto.getId(), calculatedBillDto.getId());
        assertNotEquals(paidBillDto.getAmount(), calculatedBillDto.getAmount());
        assertEquals(paidBillDto.getAmount().compareTo(BigDecimal.ZERO), 0);

        return paidBillDto;
    }

}
