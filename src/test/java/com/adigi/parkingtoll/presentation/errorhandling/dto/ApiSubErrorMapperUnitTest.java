package com.adigi.parkingtoll.presentation.errorhandling.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ApiSubErrorMapperUnitTest {

    @InjectMocks
    private ApiSubErrorMapper mapper;


    @Test
    public void givenFieldErrors_whenMapFieldErrors_getApiSubErrors() {

        // given
        FieldError fieldError1 = new FieldError("objectName1", "field", "defaultMessage");
        FieldError fieldError2 = new FieldError("objectName2", "field", new Object(), false, null, null, "defaultMessage");

        // when
        List<ApiSubError> apiSubErrors = mapper.mapFieldErrors(Arrays.asList(fieldError1, fieldError2));

        // then
        assertEquals(apiSubErrors.size(), 2);
        assertEquals(apiSubErrors.get(0).getObject(), "objectName1");
        assertEquals(apiSubErrors.get(1).getObject(), "objectName2");

    }


    @Test
    public void givenEmptyList_whenMapFieldErrors_getEmptyList() {

        // given
        List<FieldError> list = new ArrayList<>();

        // when
        List<ApiSubError> apiSubErrors = mapper.mapFieldErrors(list);

        // then
        assertEquals(apiSubErrors.size(), 0);

    }

    @Test
    public void givenObjectError_whenMapObjectErrors_getApiSubErrors() {

        // given
        ObjectError objectError1 = new ObjectError("objectError1", "defaultMessage");
        ObjectError objectError2 = new ObjectError("objectError2", "defaultMessage");
        ObjectError objectError3 = new ObjectError("objectError3", null, null, null);

        List<ObjectError> list = new ArrayList<>(Arrays.asList(objectError1, objectError2, objectError3));

        // when
        List<ApiSubError> apiSubErrors = mapper.mapObjectErrors(list);

        // then
        assertEquals(apiSubErrors.size(), 3);
        assertEquals(apiSubErrors.get(1).getObject(), objectError2.getObjectName());
        assertEquals(apiSubErrors.get(2).getObject(), objectError3.getObjectName());

    }


    @Test
    public void givenEmptyList_whenMapObjectErrors_getEmptyList() {

        // given
        List<ObjectError> list = new ArrayList<>();

        // when
        List<ApiSubError> apiSubErrors = mapper.mapObjectErrors(list);

        // then
        assertEquals(apiSubErrors.size(), 0);

    }

    @Test
    public void givenFieldError_whenMapValue_getApiSubError() {

        // given
        FieldError fieldError1 = new FieldError("objectName1", "field", new Object(), false, null, null, "defaultMessage");

        // when
        ApiSubError apiSubError = mapper.mapValue(fieldError1);

        // then
        assertEquals(apiSubError.getObject(), fieldError1.getObjectName());
        assertEquals(apiSubError.getField(), fieldError1.getField());
        assertEquals(apiSubError.getRejectedValue(), fieldError1.getRejectedValue());
        assertEquals(apiSubError.getMessage(), fieldError1.getDefaultMessage());

    }

    @Test
    public void givenObjectError_whenMapValue_getApiSubError() {

        // given
        ObjectError objectError1 = new ObjectError("objectError1", "defaultMessage");

        // when
        ApiSubError apiSubError = mapper.mapValue(objectError1);

        // then
        assertEquals(apiSubError.getObject(), objectError1.getObjectName());
        assertNull(apiSubError.getField());
        assertNull(apiSubError.getRejectedValue());
        assertEquals(apiSubError.getMessage(), objectError1.getDefaultMessage());

    }

}