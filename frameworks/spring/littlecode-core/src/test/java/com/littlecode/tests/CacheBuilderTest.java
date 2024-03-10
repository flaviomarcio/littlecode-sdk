package com.littlecode.tests;

import com.littlecode.cache.CacheBuilder;
import com.littlecode.cache.plugins.Plugin;
import com.littlecode.cache.plugins.impl.DiskPlugin;
import com.littlecode.parsers.HashUtil;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CacheBuilderTest {
    private final CacheBuilder cache = new CacheBuilder();

    //@Test
    public void UI_CHECK_Objects() {
        Assertions.assertNull(cache.loader());
        Assertions.assertNotNull(cache.seeker());
        Assertions.assertNotNull(cache.actuator());
        Assertions.assertNotNull(cache.setting());
        Assertions.assertTrue(cache.name().isEmpty());
        Assertions.assertTrue(cache.key().isEmpty());
    }

    //@Test
    public void UI_CHECK_Settings() {

        var setting = cache.setting();
        Assertions.assertNotNull(setting.expiration());
        Assertions.assertEquals(setting.expiration().getOnlyStorage(), 0);
        Assertions.assertEquals(setting.expiration().getToDiscard(), 0);

        Assertions.assertNotNull(setting.storage());
        Assertions.assertNull(setting.storage().getType());
        Assertions.assertNull(setting.storage().getUri());
        Assertions.assertNull(setting.storage().getProtocol());
        Assertions.assertNull(setting.storage().getHostName());
        Assertions.assertNull(setting.storage().getUserName());
        Assertions.assertNull(setting.storage().getPassword());
        Assertions.assertNull(setting.storage().getToken());
        Assertions.assertNull(setting.storage().getLocal());

    }

    //@Test
    public void UI_CHECK_StoragePlugins() {

        for (var storagePlugin : CacheBuilder.StorageType.listNames()) {
            Assertions.assertDoesNotThrow(() -> cache.setting().storage().setType(storagePlugin));
            cache.clear();

            Plugin target = (DiskPlugin) cache.actuator().storage();
            if (storagePlugin.isEmpty())
                Assertions.assertNull(target);
            else
                Assertions.assertNotNull(target);

            if (target == null)
                continue;

//            if(target instanceof DiskPlugin){
//                var diskPlugin= (DiskPlugin) target;
//                Assertions.assertTrue(target.items().isEmpty());
//                if(diskPlugin!=null){
//                    Assertions.assertTrue(diskPlugin.localCache().toFile().exists());
//                    Assertions.assertTrue(diskPlugin.localCache().toFile().isDirectory());
//                }
//            }

            var listData = createDataList();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                    });
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());

            target.clear();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                        Assertions.assertTrue(target.remove(itemGet.id()));
                        Assertions.assertFalse(target.remove(itemGet.id()));
                        Assertions.assertNull(target.get(id));
                    });

            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());

            target.clear();
            cache.setItems(listData);
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());
            target.clear();
            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());
        }
    }


    //@Test
    public void UI_CHECK_Actuator() {

        var target = cache.actuator();

        Assertions.assertNotNull(target.cache());
        Assertions.assertNull(target.storage());
        Assertions.assertNull(target.checkPoint());
        Assertions.assertNull(target.loader());
        Assertions.assertNotNull(target.items());
        Assertions.assertTrue(target.items().isEmpty());
        Assertions.assertDoesNotThrow(target::clear);

        CacheBuilder.StorageType.listNames().forEach(s -> cache.setting().storage().setType(s));
        CacheBuilder.StorageType.list().forEach(s -> cache.setting().storage().setType(s));

        cache.setting().storage().setType("");
        for (var storagePlugin : CacheBuilder.StorageType.listNames()) {
            cache.setting().storage().setType(storagePlugin);
            cache.clear();
            Assertions.assertTrue(target.items().isEmpty());

            var listData = createDataList();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                    });
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());

            target.clear();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                        Assertions.assertTrue(target.remove(itemGet.id()));
                        Assertions.assertFalse(target.remove(itemGet.id()));
                        Assertions.assertNull(target.get(id));
                    });

            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());

            target.clear();
            target.setItems(listData);
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());
            target.clear();
            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());
        }
    }

    //@Test
    public void UI_CHECK_CacheBuilder() {

        var target = cache;
        Assertions.assertNotNull(target.actuator());
        Assertions.assertNotNull(target.setting());
        Assertions.assertNull(target.loader());
        Assertions.assertNotNull(target.items());
        Assertions.assertTrue(target.items().isEmpty());
        Assertions.assertDoesNotThrow(target::clear);

        CacheBuilder.StorageType.listNames().forEach(s -> cache.setting().storage().setType(s));
        CacheBuilder.StorageType.list().forEach(s -> cache.setting().storage().setType(s));

        cache.setting().storage().setType("");
        for (var storagePlugin : CacheBuilder.StorageType.listNames()) {
            cache.setting().storage().setType(storagePlugin);
            target.clear();
            Assertions.assertTrue(target.items().isEmpty());

            var listData = createDataList();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                    });
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());

            target.clear();
            listData.forEach(
                    item ->
                    {
                        var id = HashUtil.toMd5Uuid(item);
                        Assertions.assertDoesNotThrow(() -> cache.actuator().put(item));
                        var itemGet = target.get(id);
                        Assertions.assertNotNull(itemGet);
                        Assertions.assertEquals(id, itemGet.id());
                        Assertions.assertEquals(id, HashUtil.toMd5Uuid(itemGet.data()));
                        Assertions.assertTrue(target.remove(id));
                        Assertions.assertFalse(target.remove(id));
                        Assertions.assertNull(target.get(id));
                    });

            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());

            target.clear();
            target.setItems(listData);
            Assertions.assertEquals(target.items().size(), listData.size());
            Assertions.assertEquals(target.itemsCount(), listData.size());
            Assertions.assertFalse(target.items().isEmpty());
            target.clear();
            Assertions.assertEquals(target.items().size(), 0);
            Assertions.assertEquals(target.itemsCount(), 0);
            Assertions.assertTrue(target.items().isEmpty());
        }
    }

    //@Test
    public void UI_CHECK_Loader() {

        cache.setting().storage().setType("");
        var target = cache;
        var listData = createDataList();
        target.clear();
        target.setLoader(new CacheBuilder.Loader() {
            @Override
            public boolean available(Object checkPoint) {
                return true;
            }

            @Override
            public boolean load(CacheBuilder cache) {
                cache.setItems(listData);
                return true;
            }

            @Override
            public Object checkPoint() {
                return null;
            }

        });

        target.load();
        Assertions.assertEquals(target.items().size(), listData.size());
        Assertions.assertEquals(target.itemsCount(), listData.size());
        Assertions.assertFalse(target.items().isEmpty());

    }


    //@Test
    public void UI_CHECK_Seeker() {
        var target = this.cache;
        var listData = createDataList();
        target.setting().storage().setType(CacheBuilder.StorageType.Disk);
        target.clear();
        target.setItems(listData);
        Assertions.assertEquals(target.items().size(), listData.size());
        Assertions.assertEquals(target.itemsCount(), listData.size());
        Assertions.assertFalse(target.items().isEmpty());
    }


    private List<Object> createDataList() {
        return List.of(
                Map.of("id", UUID.randomUUID().toString(), "name", UUID.randomUUID().toString()),
                new ItemSave(),
                new ItemSave()
        );
    }

    @Component
    @Getter
    private static class ItemSave {
        private final UUID id = UUID.randomUUID();
        private final String name = UUID.randomUUID().toString();
    }

}