package com.littlecode.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BoolToIntConverter implements AttributeConverter<Boolean, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Boolean aBoolean) {
        return aBoolean != null && aBoolean?1:0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer integer) {
        return integer != null && integer.equals(1);
    }
}
