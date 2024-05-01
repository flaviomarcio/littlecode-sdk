package com.littlecode.jpa.crud;

import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectValueUtil;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CRUDTemplate<MODEL, DTO> {

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
        return this.disable(
                ObjectValueUtil
                        .of(this)
                        .asUUID(getFieldIdName())
        );
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
