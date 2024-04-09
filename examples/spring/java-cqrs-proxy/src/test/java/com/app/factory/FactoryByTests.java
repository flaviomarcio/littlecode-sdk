package com.app.factory;

import com.app.business.model.ofservice.*;
import com.app.business.repository.ofservice.*;
import com.app.business.service.crud.*;
import com.app.business.service.event.*;
import com.littlecode.containers.ObjectReturn;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private final List<ProxyProducer> producerList = new ArrayList<>();
    private final List<ProxyGroup> groupList = new ArrayList<>();
    private final List<ProxyTarget> targetList = new ArrayList<>();
    private Environment mockEnvironment;
    private ProxyGroupRepository proxyGroupRepository;
    private ProxyTargetRevRepository proxyTargetRevRepository;
    private ProxyTargetRepository proxyTargetRepository;
    private ProxyForwarderRepository proxyForwarderRepository;
    private ProxyProducerRepository proxyProducerRepository;
    private ProxyEventItemRepository proxyEventItemRepository;
    private ProxyEventRepository proxyEventRepository;

    private CRUDProducerService crudProducerService;
    private CRUDGroupService crudGroupService;
    private CRUDTargetService crudTargetService;
    private EventQueueService eventQueueService;
    private EventService eventService;
    private EventIngesterService eventIngesterService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
        makeMockQueueService();
        setupNotifyProducer();
        setupNotifyGroup();
        setupNotifyTarget();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.proxyGroupRepository = Mockito.mock(ProxyGroupRepository.class);
        this.proxyTargetRevRepository = Mockito.mock(ProxyTargetRevRepository.class);
        this.proxyTargetRepository = Mockito.mock(ProxyTargetRepository.class);
        this.proxyForwarderRepository = Mockito.mock(ProxyForwarderRepository.class);
        this.proxyProducerRepository = Mockito.mock(ProxyProducerRepository.class);
        this.proxyEventItemRepository = Mockito.mock(ProxyEventItemRepository.class);
        this.proxyEventRepository = Mockito.mock(ProxyEventRepository.class);

        this.eventQueueService = Mockito.mock(EventQueueService.class);
        this.crudProducerService = new CRUDProducerService(proxyProducerRepository);
        this.crudGroupService = new CRUDGroupService(proxyGroupRepository);
        this.crudTargetService = new CRUDTargetService(proxyGroupRepository, proxyTargetRevRepository, proxyTargetRepository, proxyForwarderRepository);
        this.eventService = new EventService(proxyProducerRepository, proxyTargetRevRepository, proxyTargetRepository, proxyForwarderRepository, proxyEventItemRepository, proxyEventRepository);
        this.eventIngesterService = new EventIngesterService(eventQueueService, eventService);
    }

    private void makeMockEnvironment() {
        List<Map<String, String>> envList = List.of(
//                Map.of(
//                        "dispatcher.international.code", "55",
//                        "dispatcher.cleanup.days", "7",
//                        "dispatcher.strategy.whatsapp", "NONE"
//                ),
        );

        //noinspection RedundantOperationOnEmptyContainer
        envList.forEach(envMap -> {
            envMap.forEach((key, value) ->
                    {
                        Mockito.when(mockEnvironment.getProperty(key)).thenReturn(value);
                        Mockito.when(mockEnvironment.getProperty(key, "")).thenReturn(value);
                    }
            );
        });
    }

    private void makeMockQueueService() {
        Mockito.when(this.eventQueueService.send(Mockito.any())).thenReturn(ObjectReturn.Empty());
    }

    private void setupNotifyProducer() {
        producerList.clear();
        producerList.add(
                ProxyProducer
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .name("record")
                        .description("description of record")
                        .enabled(true)
                        .build()
        );

        producerList.forEach(notifyProducer -> {
            Mockito.when(proxyProducerRepository.findById(notifyProducer.getId())).thenReturn(Optional.of(notifyProducer));
            Mockito.when(proxyProducerRepository.existsById(notifyProducer.getId())).thenReturn(true);
            Mockito.when(proxyProducerRepository.save(notifyProducer)).thenReturn(notifyProducer);
        });
        Mockito.when(proxyProducerRepository.findAll()).thenReturn(producerList);
    }

    private void setupNotifyGroup() {
        groupList.clear();
        groupList.add(
                ProxyGroup
                        .builder()
                        .id(UUID.randomUUID())
                        .dtCreate(LocalDateTime.now())
                        .dtChange(LocalDateTime.now())
                        .name("record")
                        .description("description of record")
                        .enabled(true)
                        .build()
        );

        groupList.forEach(notifyGroup -> {
            Mockito.when(proxyGroupRepository.findById(notifyGroup.getId())).thenReturn(Optional.of(notifyGroup));
            Mockito.when(proxyGroupRepository.existsById(notifyGroup.getId())).thenReturn(true);
            Mockito.when(proxyGroupRepository.save(notifyGroup)).thenReturn(notifyGroup);
        });
        Mockito.when(proxyProducerRepository.findAll()).thenReturn(producerList);
    }

    private void setupNotifyTarget() {
        if (groupList.isEmpty())
            throw new RuntimeException("notifyGroupList is empty ");

        for (var group : groupList) {
            List.of(UUID.randomUUID())
                    .forEach(rev ->
                            {
                                var targetRev = ProxyTargetRev
                                        .builder()
                                        .id(rev)
                                        .dt(LocalDateTime.now())
                                        .rev(rev)
                                        .enabled(true)
                                        .build();
                                var target = ProxyTarget
                                        .builder()
                                        .id(targetRev.getRev())
                                        .dtCreate(targetRev.getDt())
                                        .targetRev(targetRev)
                                        .group(group)
                                        .name(String.format("name: %s", targetRev.getRev()))
                                        .description(String.format("description: %s", targetRev.getRev()))
                                        .enabled(true)
                                        .build();
                                targetList.add(target);
                                Mockito.when(proxyTargetRevRepository.findById(targetRev.getId())).thenReturn(Optional.of(targetRev));
                                Mockito.when(proxyTargetRepository.findById(target.getId())).thenReturn(Optional.of(target));
                                Mockito.when(proxyTargetRepository.save(target)).thenReturn(target);
                            }
                    );
        }
        Mockito.when(proxyTargetRepository.findAll()).thenReturn(targetList);

        List<ProxyForwarder> notifyDispatcherList = new ArrayList<>();
        for (var target : targetList) {
            List<ProxyForwarder> list = new ArrayList<>();
            for (var e : ProxyForwarder.Dispatcher.values()) {
                var id = UUID.randomUUID();
                var forwarder = ProxyForwarder
                        .builder()
                        .id(id)
                        .dt(target.getDtCreate())
                        .targetRev(target.getTargetRev())
                        .dispatcher(e)
                        .name(String.format("name: %s", id))
                        .enabled(true)
                        .build();
                list.add(forwarder);
                notifyDispatcherList.add(forwarder);
            }
            Mockito.when(proxyForwarderRepository.findByTargetRev(target.getTargetRev())).thenReturn(list);
        }
        Mockito.when(proxyForwarderRepository.findAll()).thenReturn(notifyDispatcherList);
    }

}
