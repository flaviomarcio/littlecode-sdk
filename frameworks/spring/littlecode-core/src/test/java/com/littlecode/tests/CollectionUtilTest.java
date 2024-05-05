package com.littlecode.tests;

import com.littlecode.parsers.CollectionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CollectionUtilTest {

    @Test
    @DisplayName("Deve validar class CollectionUtil")
    public void UT_CHECK() {

        var list = List.of(
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build()
        );

        Map<UUID, Item> map = new HashMap<>();
        list.forEach(item -> map.put(item.getId(), item));


        Assertions.assertDoesNotThrow(() -> {
            var lstChecker = CollectionUtil.toList(map);
            lstChecker.forEach(item -> Assertions.assertTrue(map.containsKey(item.getId())));
            Assertions.assertEquals(lstChecker.size(), map.size());
        });

        Assertions.assertDoesNotThrow(() -> {
            var mapChecker = CollectionUtil.toMap("id", list);
            list.forEach(item -> Assertions.assertTrue(mapChecker.containsKey(item.getId())));
            Assertions.assertEquals(mapChecker.size(), list.size());
        });

    }

    @Builder
    @Getter
    private static class Item {
        private UUID id;
        private LocalDateTime dt;
    }


}
