package com.adigi.parkingtoll.test;

import com.adigi.parkingtoll.model.persistence.entity.ParkingSlot;
import com.adigi.parkingtoll.presentation.dto.BillDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;

@Service
public class ObjectMapperService {

    @Autowired
    private ObjectMapper mapper;

    public <T> T getResponseObject(MvcResult mvcResult, Class<T> clazz)
            throws JsonProcessingException, UnsupportedEncodingException {

        return mapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
    }

    public BillDTO getBillDTO(ResultActions result)
            throws UnsupportedEncodingException, JsonProcessingException {

        return getResponseObject(result.andReturn(), BillDTO.class);
    }

    public ParkingSlot getParkingSlot(ResultActions result)
            throws UnsupportedEncodingException, JsonProcessingException {

        return getResponseObject(result.andReturn(), ParkingSlot.class);
    }
}
