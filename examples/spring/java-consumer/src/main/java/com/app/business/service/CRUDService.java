package com.app.business.service;

import com.app.business.dto.ItemModelDTO;
import com.app.business.interfaces.CRUDBaseService;
import com.app.business.model.ItemModel;
import com.app.business.repository.ItemModelRepository;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.PrimitiveUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CRUDService implements CRUDBaseService<ItemModel, ItemModelDTO> {
    private final ItemModelRepository repository;

    public ItemModelDTO outFrom(ItemModel model) {
        return ItemModelDTO
                .builder()
                .id(model.getId())
                .dtCreate(model.getDtCreate())
                .dtChange(model.getDtCreate())
                .name(model.getName())
                .title(model.getTitle())
                .description(model.getDescription())
                .icon(model.getIco())
                .route(model.getRoute())
                .enabled(model.isEnabled())
                .build();
    }


    public ObjectReturn get(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");
        var model = this.privateGet(id);
        if (model == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);
        return ObjectReturn.of(outFrom(model));
    }

    public ObjectReturn list() {
        List<ItemModelDTO> list = new ArrayList<>();
        repository.findAllByOrderByName()
                .forEach(model -> {
                    list.add(this.outFrom(model));
                });
        return ObjectReturn.of(list);
    }

    @Transactional
    public ObjectReturn saveIn(ItemModelDTO target) {
        if (target == null)
            return ObjectReturn.BadRequest(ItemModelDTO.class);

        var model =
                (target.getId() == null)
                        ? null
                        : repository.findById(target.getId()).orElse(null);

        model = ItemModel
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(model == null ? LocalDateTime.now() : model.getDtCreate())
                .dtChange(LocalDateTime.now())
                .name(target.getName())
                .description(target.getDescription())
                .enabled(target.isEnabled())
                .build();
        repository.save(model);
        return ObjectReturn.of(outFrom(model));
    }

    @Override
    public ObjectReturn delete(String id) {
        if (id == null || id.trim().isEmpty())
            return ObjectReturn.BadRequest("Invalid id, id is empty or null", id);
        var __id = PrimitiveUtil.toUUID(id);
        var model = this.privateGet(__id);
        if (model == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);
        return ObjectReturn.of(model);
    }

    @Transactional
    public ObjectReturn delete(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");
        var model = this.privateGet(id);
        return privateDelete(model);
    }

    @Transactional
    public ObjectReturn delete(ItemModelDTO target) {
        if (target == null)
            return ObjectReturn.BadRequest(ItemModelDTO.class);
        if (target.getId() == null)
            return ObjectReturn.BadRequest("Invalid %s.getId() is null", target.getClass());
        var model = this.privateGet(target.getId());
        return privateDelete(model);
    }

    private ItemModel privateGet(UUID id) {
        return
                (id == null)
                        ? null
                        : repository.findById(id).orElse(null);
    }

    private ObjectReturn privateDelete(ItemModel model) {
        if (model == null)
            return ObjectReturn.NoContent("Invalid model is null: %s", ItemModel.class);
        model.setDtChange(LocalDateTime.now());
        model.setEnabled(false);
        repository.save(model);
        return ObjectReturn.of(this.outFrom(model));
    }

}
