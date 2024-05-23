package com.littlecode.jpa.converters;

import com.littlecode.parsers.PrimitiveUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, String> {
    @Override
    public String convertToDatabaseColumn(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Override
    public UUID convertToEntityAttribute(String uuid) {
        return uuid != null ? UUID.fromString(uuid) : null;
    }
}
