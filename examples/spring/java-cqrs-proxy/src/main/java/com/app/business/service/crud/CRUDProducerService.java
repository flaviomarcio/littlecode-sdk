package com.app.business.service.crud;

import com.app.business.dto.ProducerIn;
import com.app.business.model.ofservice.ProxyProducer;
import com.app.business.repository.ofservice.ProxyProducerRepository;
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
public class CRUDProducerService extends CRUDBase<ProxyProducer, ProducerIn> {
    private final ProxyProducerRepository repository;

    public ProducerIn inFrom(ProxyProducer in) {
        return ProducerIn
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
    public ObjectReturn saveIn(ProducerIn in) {
        if (in == null)
            return ObjectReturn.BadRequest(ProducerIn.class);

        var producer =
                (in.getId() == null)
                        ? null
                        : repository.findById(in.getId()).orElse(null);

        producer = ProxyProducer
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(producer == null ? LocalDateTime.now() : producer.getDtCreate())
                .dtChange(LocalDateTime.now())
                .name(in.getName())
                .description(in.getDescription())
                .enabled(in.isEnabled())
                .build();
        repository.save(producer);
        in.setId(producer.getId());
        in.setDtCreate(producer.getDtCreate());
        in.setDtChange(producer.getDtChange());
        return ObjectReturn.of(in);
    }

    @Override
    public ObjectReturn disable(UUID id) {
        if (id == null)
            return ObjectReturn.BadRequest("Invalid id");

        var producer = repository.findById(id).orElse(null);
        if (producer == null)
            return ObjectReturn.NoContent("Invalid id: %s", id);

        if (!producer.isEnabled())
            return ObjectReturn.of(producer);

        producer.setDtChange(LocalDateTime.now());
        producer.setEnabled(false);
        repository.save(producer);
        return ObjectReturn.of(producer);
    }

    @Override
    public ObjectReturn findIn(UUID id) {
        if (id == null)
            return ObjectReturn
                    .BadRequest("Invalid id");
        var producer = repository.findById(id).orElse(null);
        if (producer == null)
            return ObjectReturn.NotFound("Invalid id: %s", id);

        return ObjectReturn.of(inFrom(producer));
    }

    @Override
    public ObjectReturn list() {
        List<ProducerIn> outList = new ArrayList<>();
        repository.findAll()
                .forEach(producer -> {
                    outList.add(inFrom(producer));
                });
        return ObjectReturn.of(outList);
    }

}
