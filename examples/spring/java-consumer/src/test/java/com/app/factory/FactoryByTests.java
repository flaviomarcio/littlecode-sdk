package com.app.factory;

import com.app.business.model.ItemModel;
import com.app.business.repository.ItemModelRepository;
import com.app.business.service.CRUDService;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Service
public class FactoryByTests {
    private final List<ItemModel> commanderModelList = new ArrayList<>();
    private Environment mockEnvironment;
    private ItemModelRepository commandRepository;
    private CRUDService commanderService;

    public FactoryByTests() {
        setupMockClasses();
        makeMockEnvironment();
        setupCommanderModel();
    }

    private void setupMockClasses() {
        this.mockEnvironment = Mockito.mock(Environment.class);
        this.commandRepository = Mockito.mock(ItemModelRepository.class);
        this.commanderService = new CRUDService(commandRepository);
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

    private void setupCommanderModel() {
        commanderModelList.clear();
        for (int i = 0; i < 100; i++) {
            commanderModelList.add(
                    ItemModel
                            .builder()
                            .id(UUID.randomUUID())
                            .dtCreate(LocalDateTime.now())
                            .dtChange(LocalDateTime.now())
                            .name("record")
                            .title("title of record")
                            .description("description of record")
                            .route(URI.create("http://tool.com"))
                            .ico(URI.create("http://tool.com/ico.ico"))
                            .enabled(true)
                            .build()
            );
        }


        commanderModelList.forEach(model -> {
            Mockito.when(commandRepository.findById(model.getId())).thenReturn(Optional.of(model));
            Mockito.when(commandRepository.existsById(model.getId())).thenReturn(true);
            Mockito.when(commandRepository.save(model)).thenReturn(model);
        });
        Mockito.when(commandRepository.findAll()).thenReturn(commanderModelList);
    }

}
