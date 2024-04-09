package com.app.business.service.event;

import com.app.business.dto.ProxyContainer;
import com.app.business.dto.EventIn;
import com.app.business.interfaces.DispatcherBase;
import com.app.business.model.ofservice.*;
import com.app.business.repository.ofservice.*;
import com.app.business.service.impl.*;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.parsers.ObjectUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final ProxyProducerRepository proxyProducerRepository;
    private final ProxyTargetRevRepository proxyTargetRevRepository;
    private final ProxyTargetRepository proxyTargetRepository;
    private final ProxyForwarderRepository proxyForwarderRepository;
    private final ProxyEventItemRepository proxyEventItemRepository;
    private final ProxyEventRepository proxyEventRepository;
    private final Map<ProxyForwarder.Dispatcher, Class<?>> interfaces = makeInterfaces();

    private static Map<ProxyForwarder.Dispatcher, Class<?>> makeInterfaces() {
        return Map.of(
                ProxyForwarder.Dispatcher.NONE, DispatcherNONE.class,
                ProxyForwarder.Dispatcher.AMQP, DispatcherAMQP.class,
                ProxyForwarder.Dispatcher.SQS, DispatcherSQS.class,
                ProxyForwarder.Dispatcher.KAFKA, DispatcherKAFKA.class,
                ProxyForwarder.Dispatcher.MQTT, DispatcherMQTT.class
        );

    }

    @Transactional
    public ObjectReturn register(EventIn event) {
        try {
            return internalRegister(event);
        } catch (Exception e) {
            return ObjectReturn.of(e);
        }
    }

    private ProxyForwarder forwarderFindById(UUID id) {
        if (id == null)
            return null;
        return proxyForwarderRepository.findById(id).orElse(null);
    }

    public ProxyContainer proxyContainerFindByTargetId(@NotNull UUID id) {
        var proxyTargetRev = proxyTargetRevRepository.findById(id).orElse(null);

        if (proxyTargetRev == null)
            return null;

        var proxyTarget = proxyTargetRepository.findById(proxyTargetRev.getRev()).orElse(null);
        if (proxyTarget == null)
            return null;

        var forwarders = proxyForwarderRepository.findByTargetRev(proxyTargetRev);

        return ProxyContainer
                .builder()
                .target(proxyTarget)
                .forwarders(forwarders)
                .build();
    }

    private ObjectReturn findProducer(EventIn.Producer producerIn) {
        if (producerIn == null)
            return ObjectReturn
                    .BadRequest(EventIn.Producer.class);

        var producer = proxyProducerRepository.findById(producerIn.getId()).orElse(null);
        if (producer == null)
            return ObjectReturn.NoContent("Invalid producer: %s", producerIn.getId());

        if (!producer.getAccessKey().equals(producerIn.getAccessKey()))
            return ObjectReturn
                    .Conflict("Invalid producer: %s", producerIn.getId());

        return ObjectReturn.of(producer);
    }

    private ObjectReturn findTargetContainer(EventIn.Target targetIn) {
        if (targetIn == null || targetIn.getTargetId() == null)
            return ObjectReturn.Empty();

        var container = this.proxyContainerFindByTargetId(targetIn.getTargetId());

        if (container == null)
            return ObjectReturn.BadRequest("Invalid target: %s", targetIn.getTargetId());

        if (!container.getTarget().isEnabled())
            return ObjectReturn.Conflict("Invalid target: %s", targetIn.getTargetId());

        if (container.getForwarders().isEmpty())
            return ObjectReturn.Conflict(ProxyForwarder.class);

        return ObjectReturn.of(container);
    }

    public List<ProxyForwarder> forwardersDistinct(List<ProxyForwarder> forwarders) {
        List<UUID> un = new ArrayList<>();
        List<ProxyForwarder> __return = new ArrayList<>();
        for (var f : forwarders) {
            if (un.contains(f.getId()))
                continue;
            __return.add(f);
            un.add(f.getId());
        }
        return __return;
    }

    private ObjectReturn createEvent(@NotNull EventIn eventIn) {
        var targetIn = eventIn.getTarget();
        if (targetIn == null)
            return ObjectReturn.BadRequest("Invalid event.target");

        var producer = this.findProducer(eventIn.getProducer());
        if (producer == null)
            return ObjectReturn.NoContent("Invalid producer: %s", eventIn.getProducer().getId());

        var event = ProxyEvent
                .builder()
                .id(UUID.randomUUID())
                .dtCreate(LocalDateTime.now())
                .producerId(eventIn.getProducer().getId())
                .targetId(targetIn.getTargetId())
                .forwarderId(targetIn.getForwarderId())
                .status(ProxyEvent.Status.WAIT)
                .payload(ObjectUtil.toString(eventIn))
                .eventItems(new ArrayList<>())
                .build();

        var __return = findTargetContainer(targetIn);
        if (!__return.isOK())
            return __return;

        var container = __return.cast(ProxyContainer.class);
        if (container == null)
            return ObjectReturn.NoContent(ProxyContainer.class);

        List<String> destinations = new ArrayList<>();
        var forwarders = container.getForwarders();
        {
            var forwarder = this.forwarderFindById(targetIn.getForwarderId());
            if (forwarder != null)
                forwarders.add(forwarder);
            forwarders = forwardersDistinct(forwarders);


            {
                List<String> destinationList = new ArrayList<>(eventIn.getTarget().getDestinations());
                if (container.getTarget() != null) {
                    for (var targetItem : container.getTarget().getItems()) {
                        if (targetItem == null)
                            continue;
                        destinationList.add(targetItem.getDestination());
                    }
                }

                for (var destination : destinationList) {
                    destination = destination.trim();
                    if (destination.isEmpty())
                        continue;
                    var contactSrc = destination.replace(" ", "");
                    var contactAr = contactSrc.split(",");
                    for (var contact : contactAr) {
                        contact = contact.trim().toLowerCase();
                        if (!destinations.contains(contact))
                            destinations.add(contact);
                    }
                }
            }
        }

        if (forwarders.isEmpty() || destinations.isEmpty())
            return ObjectReturn.of(event);

        for (var forwarder : forwarders) {
            for (var destination : destinations) {
                var eventItem = ProxyEventItem
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .event(event)
                        .forwarder(forwarder)
                        .destination(destination)
                        .status(ProxyEventItem.Status.WAITING)
                        .attempts(0)
                        .build();
                event.getItems().add(eventItem);
            }
        }
        return ObjectReturn.of(event);
    }


    private ObjectReturn internalRegister(EventIn eventIn) {
        if (eventIn == null)
            return ObjectReturn.BadRequest(EventIn.class);

        var __return = this.findProducer(eventIn.getProducer());
        if (!__return.isOK())
            return __return;


        __return = this.createEvent(eventIn);
        if (!__return.isOK())
            return __return;

        var event = __return.cast(ProxyEvent.class);

        proxyEventRepository.save(event);
        return ObjectReturn.of(event);
    }


    private DispatcherBase getNotifyDispatcher(ProxyForwarder.Dispatcher dispatcher) {
        if (dispatcher == null)
            return null;
        synchronized (interfaces) {
            var aClass = interfaces.get(dispatcher);
            if (aClass == null)
                return null;
            return ObjectUtil.create(aClass);
        }
    }

    public void dispatcher(UUID eventItemId) {
        if (eventItemId == null)
            return;
        var eventItem = proxyEventItemRepository.findById(eventItemId).orElse(null);
        if (eventItem == null) {
            log.error("Invalid [{}]: [{}]", ProxyEventItem.class, eventItemId);
            return;
        }

        try {

            var forwarder = eventItem.getForwarder();
            if (forwarder == null) {
                eventItem.setStatus(ProxyEventItem.Status.CONFLICT);
                return;
            }

            if (forwarder.getDispatcher().equals(ProxyForwarder.Dispatcher.NONE)) {
                eventItem.setStatus(ProxyEventItem.Status.SKIPPED);
                return;
            }

            DispatcherBase notifyDispatcher = getNotifyDispatcher(forwarder.getDispatcher());

            if (notifyDispatcher == null) {
                eventItem.setStatus(ProxyEventItem.Status.CONFLICT);
                return;
            }

            notifyDispatcher.dispatcher(forwarder, eventItem);

            eventItem.setStatus(ProxyEventItem.Status.SENT);
        } finally {
            proxyEventItemRepository.save(eventItem);
        }
    }

}
