package com.littlecode.tests;

import com.littlecode.parsers.CollectionUtil;
import com.littlecode.parsers.HashUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CollectionUtilTest {

    @Test
    @DisplayName("Deve validar class CollectionUtil")
    void UT_CHECK_CONSTRUCTOR() {
        final var item = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)));
        Assertions.assertThrows(NullPointerException.class, () -> new CollectionUtil((List) null));
        Assertions.assertThrows(NullPointerException.class, () -> new CollectionUtil((Map) null));
    }

    @Test
    @DisplayName("Deve validar to List")
    void UT_CHECK_TO_OBJECT_LIST() {

        final var item = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)).toObjectList());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(Map.of("a", item)).toObjectList());
    }

    @Test
    @DisplayName("Deve validar as Map")
    void UT_CHECK_AS_MAP() {

        final var item = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        List<Item> list = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)).asMap("id"));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)).asMap("xx"));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)).asMap(null));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).asMap("x"));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).asMap(""));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).asMap(null));

        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> true).asMap("x"));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> true).asMap(""));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> true).asMap(null));

        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> false).asMap("x"));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> false).asMap(""));
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> false).asMap(null));
    }

    @Test
    @DisplayName("Deve validar as List")
    void UT_CHECK_AS_LIST() {
        final var item = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        List<Item> list = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(List.of(item)).asList());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(Map.of("x", item)).asList());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).asList());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> true).asList());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).filter(item1 -> false).asList());
    }

    @Test
    @DisplayName("Deve validar as Item")
    void UT_CHECK_AS_ITEM() {
        final var item = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        List<Item> list = new ArrayList<>();
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(Map.of("x", item)).asItem());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(list).asItem());
        Assertions.assertDoesNotThrow(() -> new CollectionUtil(Map.of("x", list)).asItem());
    }

    @Test
    @DisplayName("Deve validar method filter")
    void UT_CHECK_FILTER() {


        final var itemA = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        final var itemB = Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build();
        var list = List.of(
                itemA, itemB,
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build()
        );

        {
            var oValue = new CollectionUtil(list).filter(v -> {
                return false;
            });
            Assertions.assertTrue(oValue.asList().isEmpty());
            Assertions.assertTrue(oValue.asMap("id").isEmpty());
        }

        {
            var oValue = new CollectionUtil(list).filter(v -> {
                return itemA == v;
            });
            Assertions.assertEquals(oValue.asItem(), itemA);
            Assertions.assertEquals(oValue.asList().size(), 1);
        }

        {
            var oValue = new CollectionUtil(list).filter(v -> {
                return (itemA == v || itemB == v);
            });
            Assertions.assertEquals(oValue.asItem(), itemA);
            var filterList = oValue.asList();
            Assertions.assertEquals(filterList.size(), 2);
            Assertions.assertTrue(filterList.contains(itemA));
            Assertions.assertTrue(filterList.contains(itemB));
        }
    }

    @Test
    @DisplayName("Deve validar Clone List")
    void UT_CHECK_CLONE_LIST() {
        {//com sucesso
            final var listA = List.of(
                    Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                    Item.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build()
            );
            var listB = CollectionUtil.cloneList(Item.class, listA);
            Assertions.assertEquals(listB.size(), listB.size());
            for (int i = 0; i < listB.size(); i++) {
                var itemAmd5 = HashUtil.toMd5(listA.get(i));
                var itemAmdB = HashUtil.toMd5(listB.get(i));
                Assertions.assertEquals(itemAmd5, itemAmdB);
            }
        }

        {//com erro
            final var listA = List.of(
                    ItemNoConstructor.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build(),
                    ItemNoConstructor.builder().id(UUID.randomUUID()).dt(LocalDateTime.now()).build()
            );
            Assertions.assertThrows(NullPointerException.class, () -> CollectionUtil.cloneList(ItemNoConstructor.class, listA));
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Item {
        private UUID id;
        private LocalDateTime dt;
    }

    @Builder
    @Getter
    private static class ItemNoConstructor {
        private UUID id;
        private LocalDateTime dt;
    }


}
