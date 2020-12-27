package com.adigi.parkingtoll.test.helper;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.MultiValueMap;

import java.util.function.BiFunction;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ReqPerformer {

    protected MockMvc mvc;

    /**
     * Perform a request using MockMvc
     *
     * @param operation   the operation that will be performed, ex. get / put / post
     * @param urlTemplate the request will target this URI
     * @param uriVars     path variable params
     * @return
     * @throws Exception
     */
    protected ResultActions performReq(
            BiFunction<String, Object[], MockHttpServletRequestBuilder> operation,
            String urlTemplate,
            Object... uriVars) throws Exception {

        return performReq(operation, urlTemplate, null, uriVars);
    }

    /**
     * Perform a request using MockMvc
     *
     * @param operation     the operation that will be performed, ex. get / put / post
     * @param urlTemplate   the request will target this URI
     * @param requestParams request params
     * @param uriVars       path variable params
     * @return
     * @throws Exception
     */
    protected ResultActions performReq(
            BiFunction<String, Object[], MockHttpServletRequestBuilder> operation,
            String urlTemplate,
            MultiValueMap<String, String> requestParams,
            Object... uriVars) throws Exception {

        return mvc
                .perform(operation.apply(urlTemplate, uriVars).contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    /**
     * Verify that the result status is ok and that the content is application/json
     *
     * @param resultActions
     * @return
     * @throws Exception
     */
    public ResultActions verifyOk(ResultActions resultActions) throws Exception {

        return resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    public ResultActions performGetParkingSlot(MultiValueMap<String, String> requestParams,
                                               String parkingName) throws Exception {
        return performReq((s, t) -> get(s, t).params(requestParams), REQGET_GET_PARKING_SLOT, parkingName);
    }

    public ResultActions performUpdateParkingSlotToFree(String parkingName, Long parkingSlotId) throws Exception {

        return performReq((s, t) -> put(s, t), REQPUT_UPDATE_PARKINGSLOT_TO_FREE, parkingName, parkingSlotId);
    }

    public ResultActions performGetCalculateBill(String parkingName, Long parkingSlotId) throws Exception {
        return performReq((s, t) -> get(s, t), REQGET_CALCULATE_BILL, parkingName, parkingSlotId);
    }

    public ResultActions performPayBill(String parkingName, Long parkingSlotId, Long billId) throws Exception {
        return performReq((s, t) -> put(s, t), REQPUT_PAY_BILL, parkingName, parkingSlotId, billId);
    }

}
