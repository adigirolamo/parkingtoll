package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.ParkingtollApplication;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.BillRepository;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.service.LocalDateTimeService;
import com.adigi.parkingtoll.test.ObjectMapperService;
import com.adigi.parkingtoll.test.helper.ReqParamsHelper;
import com.adigi.parkingtoll.test.helper.ReqPerformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.NAME_PARKING2;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ParkingtollApplication.class})
@Transactional
public class BillControllerIntegrationTest extends ReqPerformer {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ReqParamsHelper helper;

    @Autowired
    private ObjectMapperService mapper;

    @Autowired
    private BillRepository billRepository;

    @SpyBean
    private LocalDateTimeService timeService;

    private LocalDateTime paymentTimeMock;
    private String parking;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        helper.clearParams();

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
     * Checks that when it is calculated the bill - for a leaving car - it is returned the correct bill:
     * <ul>
     *     <li>BillDTO amount is not null</li>
     *     <li>Reservation payment time is the same payment time</li>
     *     <li>Reservation payed is still false</li>
     *     <li>Parking Slot id, retrieved from bill, is the same as the parking slot returned from GetParkingSlot</li>
     * </ul>
     * <p>
     *
     * @throws Exception
     */
    @Test
    public void givenParkingAndParkingSlot_whenCalculateBillForLeavingCar_getCorrectBill() throws Exception {

        // given
        helper.addPlate("PLATE").addEngineType(EngineType.ELECTRIC_20KW.toString());
        Mockito.when(timeService.getNow()).thenReturn(paymentTimeMock);

        // when
        long parkingSlotId = performGetParkingSlotGetId(parking);
        ResultActions resultCalculatedBill = performGetCalculateBill(parking, parkingSlotId);

        // then
        BillDTO billDTO = verifyResCalculatedBill(resultCalculatedBill);
        verifyEntitiesForCalculateBillWhenLeavingCar(parkingSlotId, billDTO.getId(), paymentTimeMock);
    }

    /**
     * Checks that when it pays the bill - for a leaving car - it is returned the correct bill:
     * <ul>
     *     <li>BillDTO amount is null</li>
     *     <li>Reservation payed is true</li>
     * </ul>
     * <p>
     *
     * @throws Exception
     */
    @Test
    public void givenParkingParkingSlotIdAndBillId_whenPayBill_getCorrectBill() throws Exception {

        // given
        helper.addPlate("PLATE").addEngineType(EngineType.ELECTRIC_20KW.toString());
        Mockito.when(timeService.getNow()).thenReturn(paymentTimeMock);

        // when
        long parkingSlotId = performGetParkingSlotGetId(parking);
        BillDTO calculatedBillDto = mapper.getBillDTO(performGetCalculateBill(parking, parkingSlotId));
        ResultActions payedBill = performPayBill(parking, parkingSlotId, calculatedBillDto.getId());

        // then
        BillDTO payedBillDto = verifyResPayBill(payedBill, calculatedBillDto);
        verifyEntitiesForPayBillWhenLeavingCar(parkingSlotId, payedBillDto.getId());
    }

    private Long performGetParkingSlotGetId(String parking) throws Exception {

        MvcResult resultGetParkingSlot = performGetParkingSlot(helper.getRequestParams(), parking).andReturn();

        return mapper.getResponseObject(resultGetParkingSlot, ParkingSlot.class).getId();
    }

    private void verifyEntitiesForCalculateBillWhenLeavingCar(Long parkingSlotId, Long billId, LocalDateTime paymentTimeMock) {

        Reservation reservation = billRepository.findById(billId).get().getReservation();

        assertEquals(reservation.getParkingSlot().getId(), parkingSlotId);
        assertEquals(reservation.getLocalPaymentDateTime(), paymentTimeMock);
        assertFalse(reservation.getPayed());
    }

    private void verifyEntitiesForPayBillWhenLeavingCar(Long parkingSlotId, Long billId) {

        Reservation reservation = billRepository.findById(billId).get().getReservation();

        assertEquals(reservation.getParkingSlot().getId(), parkingSlotId);
        assertTrue(reservation.getPayed());
    }


    private BillDTO verifyResCalculatedBill(ResultActions resultBill) throws Exception {

        verifyOk(resultBill);

        BillDTO bill = mapper.getResponseObject(resultBill.andReturn(), BillDTO.class);

        assertNotEquals(bill.getAmount().compareTo(BigDecimal.ZERO), 0);

        return bill;
    }

    private BillDTO verifyResPayBill(ResultActions payedBill, BillDTO calculatedBillDto) throws Exception {

        verifyOk(payedBill);

        BillDTO payedBillDto = mapper.getResponseObject(payedBill.andReturn(), BillDTO.class);

        assertEquals(payedBillDto.getId(), calculatedBillDto.getId());
        assertNotEquals(payedBillDto.getAmount(), calculatedBillDto.getAmount());
        assertEquals(payedBillDto.getAmount().compareTo(BigDecimal.ZERO), 0);

        return payedBillDto;
    }

}
