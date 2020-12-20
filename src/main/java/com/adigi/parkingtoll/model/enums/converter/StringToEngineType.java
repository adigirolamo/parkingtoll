package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.EngineType;
import org.springframework.core.convert.converter.Converter;


public class StringToEngineType implements Converter<String, EngineType> {
    @Override
    public EngineType convert(String from) {
        return EngineType.valueOf(from);
    }
}
