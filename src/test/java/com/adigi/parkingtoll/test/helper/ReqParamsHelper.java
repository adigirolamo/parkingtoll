package com.adigi.parkingtoll.test.helper;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.adigi.parkingtoll.test.constant.PresentationConstant.PARAM_ENGINETYPE;
import static com.adigi.parkingtoll.test.constant.PresentationConstant.PARAM_PLATE;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReqParamsHelper {

    private MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();

    public ReqParamsHelper clearParams() {
        requestParams.clear();
        return this;
    }

    public MultiValueMap<String, String> getRequestParams() {
        return requestParams;
    }

    public ReqParamsHelper addParam(String key, String value) {
        requestParams.add(key, value);
        return this;
    }

    public ReqParamsHelper addPlate(String plate) {
        return addParam(PARAM_PLATE, plate);
    }

    public ReqParamsHelper addEngineType(String engine) {
        return addParam(PARAM_ENGINETYPE, engine);
    }

    public String getFirstEngineType() {
        return requestParams.getFirst(PARAM_ENGINETYPE);
    }
}
