package com.app.business.interfaces;

import com.littlecode.containers.ObjectReturn;

import java.util.UUID;

public interface CRUDBaseService<MODEL, DTO> {

    DTO outFrom(MODEL in);

    ObjectReturn saveIn(DTO in);

    ObjectReturn delete(String id);

    ObjectReturn delete(DTO in);

    ObjectReturn delete(UUID id);

    ObjectReturn get(UUID id);

}
