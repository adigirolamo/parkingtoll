package com.adigi.parkingtoll.presentation.controller;

import com.adigi.parkingtoll.ParkingtollApplication;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.persistance.entity.ParkingSlot;
import com.adigi.parkingtoll.model.persistance.entity.Reservation;
import com.adigi.parkingtoll.model.persistance.repository.ParkingSlotRepository;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import com.adigi.parkingtoll.test.ObjectMapperService;
import com.adigi.parkingtoll.test.helper.ReqParamsHelper;
import com.adigi.parkingtoll.test.helper.ReqPerformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {ParkingtollApplication.class})
@Transactional
class ParkingSlotControllerIntegrationTest extends ReqPerformer {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private ReqParamsHelper helper;

    @Autowired
    private ObjectMapperService mapper;


    public String PLATE = "PLATE";

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        helper.clearParams();
    }


    @Test
    public void givenWac_whenServletContext_thenItProvidesBillController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("parkingSlotController"));
    }

    /**
     * Checks that when a Parking slot is request, it is returned the correct parking slot:
     * <ul>
     *     <li>parking slot</li>
     *     <ul>
     *         <li>parking is the same as the request one</li>
     *         <li>engine type is the same as the request one</li>
     *         <li>reserved is set to true</li>
     *     </ul>
     * </ul>
     * <p>
     * It also checks if the reservation and bill, related to the Parking slot, are in the correct status:
     * reservation:
     * <ul>
     *     <li>reservation</li>
     *     <ul>
     *         <li>plate is the same as the plate in the request</li>
     *         <li>LocalArriveDateTime is populated</li>
     *         <li>LocalDepartureDateTime is NOT populated</li>
     *         <li>LocalPaymentDateTime is NOT populated</li>
     *     </ul>
     *     <li>bill</li>
     *     <ul>
     *         <li>Amount is zero</li>
     *     </ul>
     * </ul>
     *
     * @throws Exception
     */
    @Test
    public void givenParkingPlateAndEngine_whenGetParkingSlot_getCorrectSlot() throws Exception {

        // given
        helper.addPlate(PLATE).addEngineType(EngineType.GASOLINE.toString());

        // when
        ResultActions getParkingSlotReq = performGetParkingSlot(helper.getRequestParams(), NAME_PARKING1);

        // then
        // verify correct response
        ParkingSlotDTO parkingSlotDTO = verifyGetParkingSlot(getParkingSlotReq, EngineType.GASOLINE.toString(), NAME_PARKING1, true);

        // verify correct entity status
        ParkingSlot parkingSlot = parkingSlotRepository.findById(parkingSlotDTO.getId()).get();
        verifyReservationtForIncomingCar(parkingSlot, PLATE);
        verifyBillForIncomingCar(parkingSlot);
    }

    /**
     * Checks that when there is not anymore a parking slot of the correct engine it is returned status 404
     *
     * @throws Exception
     */
    @Test
    public void givenSameParkingSameEngine_whenRequestMultipleGetParkingSlots_getNotFoundStatus() throws Exception {

        // given
        MultiValueMap<String, String> requestParams = helper.addPlate(PLATE).addEngineType(EngineType.GASOLINE.toString()).getRequestParams();

        // when
        performVerifyGetParkingSlot(requestParams, NAME_PARKING1, helper.getFirstEngineType());
        performVerifyGetParkingSlot(requestParams, NAME_PARKING1, helper.getFirstEngineType());
        performGetParkingSlot(helper.getRequestParams(), NAME_PARKING1)
                // then
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    public void givenDifferentParking_whenRequestMultipleGetParkingSlots_getParkingSlotFromDifferentParking() throws Exception {
        // given
        MultiValueMap<String, String> requestParams = helper.addPlate(PLATE).addEngineType(EngineType.GASOLINE.toString()).getRequestParams();

        // when
        performGetParkingSlot(requestParams, NAME_PARKING1);
        performGetParkingSlot(requestParams, NAME_PARKING1);
        performGetParkingSlot(requestParams, NAME_PARKING1);

        // then verify
        performVerifyGetParkingSlot(requestParams, NAME_PARKING2, helper.getFirstEngineType());
    }

    @Test
    public void givenEngineTypeElectric50_whenGetParkingSlot_getSlotForEngineE50W() throws Exception {

        // given
        final String engineType = EngineType.ELECTRIC_50KW.toString();
        helper.addPlate(PLATE).addEngineType(engineType);

        // when
        // then
        performVerifyGetParkingSlot(helper.getRequestParams(), NAME_PARKING2, engineType);
    }

    @Test
    public void givenEngineTypeElectric20_whenGetParkingSlots_getSlotForEngineE20W() throws Exception {

        // given
        final String engineType = EngineType.ELECTRIC_20KW.toString();
        helper.addPlate(PLATE).addEngineType(engineType);

        // when
        // then
        performVerifyGetParkingSlot(helper.getRequestParams(), NAME_PARKING2, engineType);
    }

    @Test
    public void givenWrongParkingName_whenGetParkingSlot_get404() throws Exception {

        // given
        helper.addPlate(PLATE).addEngineType(EngineType.GASOLINE.toString());

        // when
        ResultActions wrong_parking = performGetParkingSlot(helper.getRequestParams(), "WRONG_PARKING");

        // then
        wrong_parking.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void whenGetParkingSlot_whenUpdateParkingSlotToFree_getParkingSlotReserved() throws Exception {

        // given
        String engingeType = EngineType.ELECTRIC_50KW.toString();
        helper.addPlate(PLATE).addEngineType(engingeType);

        // when
        MvcResult mvcResult = performGetParkingSlot(helper.getRequestParams(), NAME_PARKING2).andReturn();
        long id = mapper.getResponseObject(mvcResult, ParkingSlot.class).getId();
        BillDTO calculatedBillDto = mapper.getBillDTO(performGetCalculateBill(NAME_PARKING2, id));
        performPayBill(NAME_PARKING2, id, calculatedBillDto.getId());
        ResultActions updatedParkingSlot = performUpdateParkingSlotToFree(NAME_PARKING2, id);

        // then verify
        verifyGetParkingSlot(updatedParkingSlot, engingeType, NAME_PARKING2, false);
        verifyReservationtForLeavingCar(parkingSlotRepository.findById(id).get());

    }

    @Test
    public void whenUpdateParkingSlotToFree_whenGetParkingSlot_getCorrectStatusOfParkingSlot() throws Exception {

        // given
        String engingeType = EngineType.ELECTRIC_20KW.toString();
        helper.addPlate(PLATE).addEngineType(engingeType);

        // when
        MvcResult mvcResult = performGetParkingSlot(helper.getRequestParams(), NAME_PARKING2).andReturn();
        long id = mapper.getResponseObject(mvcResult, ParkingSlot.class).getId();
        BillDTO calculatedBillDto = mapper.getBillDTO(performGetCalculateBill(NAME_PARKING2, id));
        performPayBill(NAME_PARKING2, id, calculatedBillDto.getId());
        performUpdateParkingSlotToFree(NAME_PARKING2, id);

        // then
        performVerifyGetParkingSlot(helper.getRequestParams(), NAME_PARKING2, engingeType);

        // verify correct entity status
        verifyEntitiesStatusForIncomingCar(id, PLATE);
    }

    /**
     * Perform request GetParkingSlots and verify its response
     *
     * @param requestParams
     * @throws Exception
     */
    private void performVerifyGetParkingSlot(MultiValueMap<String, String> requestParams,
                                             String parkingName,
                                             String engineType) throws Exception {
        ResultActions getParkingSlotReq = performGetParkingSlot(requestParams, parkingName);

        // then
        verifyGetParkingSlot(getParkingSlotReq, engineType, parkingName, true);
    }


    /**
     * verify that the ResultAction of getParkingSlot is in correct state
     *
     * @param resultParkingSlot ResultAction of getParkingSlot request
     * @param engineType        expected engineType
     * @param parkingName       expected parkingName
     * @param reserved          expected reserved
     * @return ParkingSlotDTO mapped from resultParkingSlot
     * @throws Exception
     */
    private ParkingSlotDTO verifyGetParkingSlot(ResultActions resultParkingSlot,
                                                String engineType,
                                                String parkingName,
                                                Boolean reserved)
            throws Exception {

        verifyOk(resultParkingSlot);

        resultParkingSlot
                .andExpect(MockMvcResultMatchers.jsonPath("$." + PARAM_ENGINETYPE).value(engineType))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + PARAM_PARKING_NAMEUID).value(parkingName))
                .andExpect(MockMvcResultMatchers.jsonPath("$." + PARAM_RESERVED).value(reserved));

        return mapper.getResponseObject(resultParkingSlot.andReturn(), ParkingSlotDTO.class);
    }

    private BillDTO verifyResCalculatedBill(ResultActions resultBill) throws Exception {

        verifyOk(resultBill);

        BillDTO bill = mapper.getResponseObject(resultBill.andReturn(), BillDTO.class);

        assertNotEquals(bill.getAmount().compareTo(BigDecimal.ZERO), 0);

        return bill;
    }

    private void verifyEntitiesStatusForIncomingCar(Long parkingSlotId, String plate) {

        ParkingSlot parkingSlot = parkingSlotRepository.findById(parkingSlotId).get();
        verifyReservationtForIncomingCar(parkingSlot, plate);
        verifyBillForIncomingCar(parkingSlot);
    }

    private void verifyReservationtForIncomingCar(ParkingSlot parkingSlot, String plate) {

        Reservation reservation = parkingSlot.getReservation();

        assertEquals(reservation.getPlate(), plate);
        assertNotNull(reservation.getLocalArriveDateTime());
        assertNull(reservation.getLocalDepartureDateTime());
        assertNull(reservation.getLocalPaymentDateTime());
    }

    private void verifyReservationtForLeavingCar(ParkingSlot parkingSlot) {

        Reservation reservation = parkingSlot.getReservation();

        assertNull(reservation.getPlate());
        assertNotNull(reservation.getLocalArriveDateTime());
        assertNotNull(reservation.getLocalDepartureDateTime());
    }

    private void verifyBillForIncomingCar(ParkingSlot parkingSlot) {
        assertEquals(parkingSlot.getReservation().getBill().getAmount().compareTo(BigDecimal.ZERO), 0);
    }
}