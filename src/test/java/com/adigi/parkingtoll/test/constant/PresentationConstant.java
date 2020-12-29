package com.adigi.parkingtoll.test.constant;

public interface PresentationConstant {
    public String CONTENT_TYPE = "application/json";

    // Request URI
    public String BASE_URL = "http://localhost:8080";
    public String REQGET_GET_PARKING_SLOT = "/parkings/{parkingNameUid}/parkingslots";
    public String REQGET_GET_PARKING_SLOT_REQ_PARAM = "/parkings/{parkingNameUid}/parkingslots?plate={idplate}&engineType={enginetype}";
    public String REQPUT_UPDATE_PARKINGSLOT_TO_FREE = "/parkings/{parkingNameUid}/parkingslots/{parkingSlotId}";
    public String REQGET_CALCULATE_BILL = REQPUT_UPDATE_PARKINGSLOT_TO_FREE + "/reservation/bill";
    public String REQPUT_PAY_BILL = REQGET_CALCULATE_BILL + "/{billId}";

    // Request parmas
    public String PARAM_PLATE = "plate";
    public String PARAM_ENGINETYPE = "engineType";
    public String PARAM_PARKING_NAMEUID = "parkingNameUid";
    public String PARAM_RESERVED = "reserved";

    // Parmas value
    public String NAME_PARKING1 = "PARKING1";
    public String NAME_PARKING2 = "PARKING2";
}
