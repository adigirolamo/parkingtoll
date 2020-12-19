package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.Currency;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter
        extends AbstractConverter<Currency>
        implements AttributeConverter<Currency, String> {

    @Override
    public Currency convertToEntityAttribute(String code) {
        return convertToEntityAttribute(Currency.values(), code);
    }
}
