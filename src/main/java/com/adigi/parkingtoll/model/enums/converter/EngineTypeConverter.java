package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.EngineType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EngineTypeConverter
        extends AbstractConverter<EngineType>
        implements AttributeConverter<EngineType, String> {

    @Override
    public EngineType convertToEntityAttribute(String code) {
        return convertToEntityAttribute(EngineType.values(), code);
    }
}
