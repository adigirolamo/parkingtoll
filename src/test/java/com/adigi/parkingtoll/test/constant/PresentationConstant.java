package com.adigi.parkingtoll.test.constant;

public interface PresentationConstant {
    public String CONTENT_TYPE = "application/json";

    // Request URI
    public String REQGET_GET_PARKING_SLOT = "/parking/{parkingNameUid}/parkingslot";
    public String REQPUT_UPDATE_PARKINGSLOT_TO_FREE = "/parking/{parkingNameUid}/parkingslot/{parkingSlotId}";
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
