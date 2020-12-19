package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.BaseEnum;

import java.util.stream.Stream;

public class AbstractConverter<T extends Enum<T> & BaseEnum> {

    public String convertToDatabaseColumn(T enumType) {
        if (enumType == null) {
            return null;
        }
        return enumType.getCode();
    }

    public T convertToEntityAttribute(T[] enumValues, String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(enumValues)
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
