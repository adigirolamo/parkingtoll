package com.adigi.parkingtoll.presentation.errorhandling;

import com.adigi.parkingtoll.ParkingtollApplication;
import com.adigi.parkingtoll.presentation.dto.ParkingSlotDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.REQGET_GET_PARKING_SLOT_REQ_PARAM;
import static com.adigi.parkingtoll.test.constant.PresentationConstant.REQPUT_UPDATE_PARKINGSLOT_TO_FREE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = {ParkingtollApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CustomRestExceptionHandlerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private HttpHeaders headers;
    private HttpEntity request;

    @TestConfiguration(proxyBeanMethods = false)
    static class Config {

        @Bean
        RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
                    .setReadTimeout(Duration.ofSeconds(1));
        }

    }

    @BeforeEach
    public void setUp() {
        setupHttpHeaders();
        setupHttpEntity();
    }

    private void setupHttpHeaders() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    private void setupHttpEntity() {
        request = new HttpEntity(headers);
    }

    @Test
    public void givenMissingServletRequestParameterException_whenRequest_getBAD_REQUEST() {

        // given
        String missingPlate = "/parkings/{parkingNameUid}/parkingslots?&engineType={enginetype}";

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                missingPlate,
                HttpMethod.GET,
                request,
                Object.class,
                "PARKING1", "GASOLINE"
        );

        // then
        verifyStatusMessage(response, BAD_REQUEST, "parameter is missing");
    }

    @Test
    public void givenDataIntegrityViolationException_whenRequest_getBAD_REQUEST() {

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM, HttpMethod.GET, request, Object.class,
                "PARKING2", "plate", "GASOLINE"
        );

        ResponseEntity<Object> errorResponse = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM, HttpMethod.GET, request, Object.class,
                "PARKING1", "plate", "GASOLINE"
        );

        // then
        verifyStatusMessage(errorResponse, BAD_REQUEST, "SQL");
    }

    @Test
    public void givenWrongStateException_whenRequest_getBAD_REQUEST() {

        // when
        ResponseEntity<ParkingSlotDTO> parkingDto = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM, HttpMethod.GET, request,
                ParkingSlotDTO.class, "PARKING1", "aa", "GASOLINE");

        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQPUT_UPDATE_PARKINGSLOT_TO_FREE,
                HttpMethod.PUT,
                request,
                Object.class,
                "PARKING1", parkingDto.getBody().getId()
        );

        // then
        verifyStatusMessage(response, BAD_REQUEST, "Allowed state from");
    }

    @Test
    public void givenMethodArgumentTypeMismatchException_whenRequest_getBAD_REQUEST() {

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM,
                HttpMethod.GET,
                request,
                Object.class,
                "PARKING1", "plate", "GASOLINEz"
        );

        // then
        verifyStatusMessage(response, BAD_REQUEST, "Failed to convert value");
    }

    @Test
    public void givenConstraintViolationException_whenRequest_getBAD_REQUEST() {

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM,
                HttpMethod.GET,
                request,
                Object.class,
                "PARKING1", "", "GASOLINE"
        );

        // then
        verifyStatusMessage(response, BAD_REQUEST, "plate: must not be blank");
    }


    @Test
    public void givenHttpRequestMethodNotSupportedException_whenRequest_getMETHOD_NOT_ALLOWED() {

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM,
                HttpMethod.POST,
                request,
                Object.class,
                "PARKING1", "plate", "GASOLINE"
        );

        // then
        verifyStatusMessage(response, METHOD_NOT_ALLOWED, "method is not supported for this request");
    }


    @Test
    public void givenNoHandlerFoundException_whenRequest_getNOT_FOUND() {

        // given
        String wrongUri = "/parkingZZ";

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                wrongUri,
                HttpMethod.GET,
                request,
                Object.class
        );

        // then
        verifyStatusMessage(response, NOT_FOUND, "No handler found for method");
    }

    @Test
    public void givenEntityNotFoundException_whenRequest_getNOT_FOUND() {

        // when
        ResponseEntity<Object> response = testRestTemplate.exchange(
                REQGET_GET_PARKING_SLOT_REQ_PARAM,
                HttpMethod.GET,
                request,
                Object.class,
                "PARKING_WRONG", "plate", "GASOLINE"
        );

        // then
        verifyStatusMessage(response, NOT_FOUND, "Not found free ParkingSlot");
    }

    private void verifyStatusMessage(ResponseEntity<Object> response, HttpStatus status, String message) {
        assertThat(response.getStatusCode(), equalTo(status));
        assertThat(getMessage(response), containsString(message));
    }

    private String getMessage(ResponseEntity<Object> response) {
        return ((LinkedHashMap) response.getBody()).get("message").toString();
    }
}