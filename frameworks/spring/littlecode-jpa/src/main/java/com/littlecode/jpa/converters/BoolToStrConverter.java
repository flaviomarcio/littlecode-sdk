package com.littlecode.jpa.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BoolToStrConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean aBoolean) {
        return aBoolean != null && aBoolean?"1":"0";
    }

    @Override
    public Boolean convertToEntityAttribute(String v) {
        if(v==null)
            return false;
        v=v.trim().toLowerCase();
        if(v.isEmpty())
            return false;
        return v.equals("1") || v.equals("t") || v.equals("v") || v.equals("y") || v.equals("true") || v.equals("yes");
    }
}
