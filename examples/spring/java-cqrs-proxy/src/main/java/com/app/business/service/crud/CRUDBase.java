package com.app.business.service.crud;

import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CRUDBase<MODEL, DTO> {

    private static final String __ID = "id";

    public String getFieldIdName() {
        return __ID;
    }

    public DTO inFrom(MODEL in) {
        throw new RuntimeException("No Implemented");
    }

    public ObjectReturn saveIn(DTO in) {
        throw new RuntimeException("No Implemented");
    }

    public ObjectReturn disable(String id) {
        return this.disable(HashUtil.toUuid(id));
    }

    public ObjectReturn disable(DTO in) {
        if (in == null)
            return ObjectReturn.BadRequest("Invalid id");

        var fieldName = getFieldIdName();

        var property = ObjectUtil.toFieldByName(in.getClass(), fieldName);

        if (property == null)
            return ObjectReturn.Fail("Invalid model id, object: %s", in.getClass().getName());
        try {
            var idValue = (UUID) property.get(this);
            if (idValue == null)
                return ObjectReturn.BadRequest("Invalid id");

            return this.disable(idValue);
        } catch (IllegalAccessException e) {
            return ObjectReturn.of(e);
        }
    }

    public ObjectReturn disable(UUID id) {
        throw new RuntimeException("No Implemented");
    }

    public ObjectReturn findIn(UUID id) {
        throw new RuntimeException("No Implemented");
    }

    public ObjectReturn list() {
        throw new RuntimeException("No Implemented");
    }
}
