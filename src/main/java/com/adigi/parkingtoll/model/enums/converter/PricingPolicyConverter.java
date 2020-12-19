package com.adigi.parkingtoll.model.enums.converter;

import com.adigi.parkingtoll.model.enums.PricingPolicy;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PricingPolicyConverter
        extends AbstractConverter<PricingPolicy>
        implements AttributeConverter<PricingPolicy, String> {

    @Override
    public PricingPolicy convertToEntityAttribute(String code) {
        return convertToEntityAttribute(PricingPolicy.values(), code);
    }
}
