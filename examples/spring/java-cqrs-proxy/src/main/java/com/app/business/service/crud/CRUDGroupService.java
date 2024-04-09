package com.app.business.service.crud;

import com.app.business.dto.GroupIn;
import com.app.business.model.ofservice.ProxyGroup;
import com.app.business.repository.ofservice.ProxyGroupRepository;
import com.littlecode.containers.ObjectReturn;
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
public class CRUDGroupService extends CRUDBase<ProxyGroup, GroupIn> {
    private final ProxyGroupRepository repository;

    public GroupIn inFrom(ProxyGroup in) {
        return GroupIn
                .builder()
                .id(in.getId())
                .dtCreate(in.getDtCreate())
                .dtChange(in.getDtCreate())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
    }

    @Transactional
    public ObjectReturn saveIn(GroupIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(GroupIn.class);

        var group =
                (in.getId() == null)
                        ? null
                        : repository.findById(in.getId()).orElse(null);

        group = ProxyGroup
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(group == null ? LocalDateTime.now() : group.getDtCreate())
                .dtChange(LocalDateTime.now())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
        repository.save(group);
        in.setId(group.getId());
        in.setDtCreate(group.getDtCreate());
        in.setDtChange(group.getDtChange());
        return ObjectReturn.of(in);
    }

    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var group = repository.findById(id).orElse(null);
        if (group == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);

        group.setDtChange(LocalDateTime.now());
        group.setEnabled(false);
        repository.save(group);
        return ObjectReturn.of(group);
    }

    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var group = repository.findById(id).orElse(null);
        if (group == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(group));
    }

    public ObjectReturn list() {
        List<GroupIn> outList = new ArrayList<>();
        repository.findAll()
                .forEach(group -> {
                    outList.add(inFrom(group));
                });
        return ObjectReturn.of(outList);
    }

}
