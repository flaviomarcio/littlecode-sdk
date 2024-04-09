package com.app.business.service.crud;

import com.app.business.dto.TargetIn;
import com.app.business.model.ofservice.ProxyForwarder;
import com.app.business.model.ofservice.ProxyTarget;
import com.app.business.model.ofservice.ProxyTargetRev;
import com.app.business.repository.ofservice.ProxyForwarderRepository;
import com.app.business.repository.ofservice.ProxyGroupRepository;
import com.app.business.repository.ofservice.ProxyTargetRepository;
import com.app.business.repository.ofservice.ProxyTargetRevRepository;
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
public class CRUDTargetService extends CRUDBase<ProxyTarget, TargetIn> {
    private final ProxyGroupRepository proxyGroupRepository;
    private final ProxyTargetRevRepository proxyTargetRevRepository;
    private final ProxyTargetRepository proxyTargetRepository;
    private final ProxyForwarderRepository proxyForwarderRepository;


    @Override
    public TargetIn inFrom(ProxyTarget notifyTarget) {
        return TargetIn
                .builder()
                .id(notifyTarget.getTargetRev().getId())
                .dtCreate(notifyTarget.getDtCreate())
                .dtChange(notifyTarget.getDtCreate())
                .groupId(notifyTarget.getGroup().getId())
                .name(notifyTarget.getName())
                .description(notifyTarget.getDescription())
                .enabled(notifyTarget.isEnabled())
                .build();
    }

    @Transactional
    public ObjectReturn saveIn(TargetIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(ProxyForwarder.class);

        if (!proxyGroupRepository.existsById(in.getGroupId()))
            return ObjectReturn.BadRequest("Invalid target.groupId: {}", in.getGroupId());

        var targetRev =
                (in.getId() == null)
                        ? null
                        : proxyTargetRevRepository.findById(in.getId()).orElse(null);

        if (targetRev == null)
            targetRev = ProxyTargetRev
                    .builder()
                    .id(UUID.randomUUID())
                    .dt(LocalDateTime.now())
                    .rev(null)
                    .enabled(true)
                    .build();

        targetRev.setRev(UUID.randomUUID());

        var target = ProxyTarget
                .builder()
                .id(targetRev.getRev())
                .dtCreate(targetRev.getDt())
                .dtChange(LocalDateTime.now())
                .targetRev(targetRev)
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();

        List<ProxyForwarder> forwarderList = new ArrayList<>();
        for (var setting : in.getDispatchers()) {
            forwarderList.add(
                    ProxyForwarder
                            .builder()
                            .id(UUID.randomUUID())
                            .dt(target.getDtCreate())
                            .targetRev(targetRev)
                            .enabled(setting.isEnabled())
                            .build()
            );
        }

        proxyTargetRevRepository.save(targetRev);
        proxyTargetRepository.save(target);
        proxyForwarderRepository.saveAll(forwarderList);
        in.setId(target.getTargetRev().getId());
        in.setDtChange(target.getDtCreate());
        return ObjectReturn.of(in);
    }

    @Override
    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var targetRev = proxyTargetRevRepository.findById(id).orElse(null);
        if (targetRev == null)
            return ObjectReturn.NoContent("Invalid id: {}", id);

        targetRev.setRev(null);
        targetRev.setEnabled(false);
        proxyTargetRevRepository.save(targetRev);
        return ObjectReturn.Empty();
    }

    @Override
    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");
        var targetRev = proxyTargetRevRepository.findById(id).orElse(null);
        if (targetRev == null)
            return ObjectReturn.BadRequest("Invalid id: %s", id);

        var target = targetRev.getRev() == null
                ? null
                : proxyTargetRepository.findById(targetRev.getRev())
                .orElse(null);
        if (target == null)
            return ObjectReturn.NotFound("Invalid id %s", id);

        var out = inFrom(target);

        out.setDispatchers(new ArrayList<>());
        proxyForwarderRepository
                .findByTargetRev(targetRev)
                .forEach(forwarder -> {
                    out.getDispatchers()
                            .add(
                                    TargetIn.Dispatcher
                                            .builder()
                                            .name(forwarder.getName())
                                            .enabled(forwarder.isEnabled())
                                            .dispatcher(forwarder.getDispatcher())
                                            .build()
                            );
                });

        return ObjectReturn.of(out);
    }

    @Override
    public ObjectReturn list() {
        List<TargetIn> outList = new ArrayList<>();
        proxyTargetRepository.findAll()
                .forEach(notifyTarget -> {
                    outList.add(inFrom(notifyTarget));
                });
        return ObjectReturn.of(outList);
    }

    public ObjectReturn dispatchers() {
        return ObjectReturn.of(List.of(ProxyForwarder.Dispatcher.values()));
    }

}
