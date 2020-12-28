package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.ParkingSlotState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ParkingSlotStateConverter
        extends AbstractConverter<ParkingSlotState>
        implements AttributeConverter<ParkingSlotState, String> {

    @Override
    public ParkingSlotState convertToEntityAttribute(String code) {
        return convertToEntityAttribute(ParkingSlotState.values(), code);
    }
}
