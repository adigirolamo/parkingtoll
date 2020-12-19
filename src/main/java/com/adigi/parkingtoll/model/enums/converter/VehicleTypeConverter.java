package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.VehicleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class VehicleTypeConverter
        extends AbstractConverter<VehicleType>
        implements AttributeConverter<VehicleType, String> {

    @Override
    public VehicleType convertToEntityAttribute(String code) {
        return convertToEntityAttribute(VehicleType.values(), code);
    }
}
