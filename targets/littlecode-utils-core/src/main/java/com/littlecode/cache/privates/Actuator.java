package com.littlecode.cache.privates;

import com.littlecode.cache.CacheBuilder;
import com.littlecode.cache.plugins.Plugin;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Actuator {
    private final CacheBuilder cache;
    private final ConcurrentHashMap<UUID, CacheBuilder.CacheItem> itemsMap = new ConcurrentHashMap<>();
    private final List<Object> items = new ArrayList<>();
    private Plugin storage;
    private Object checkPoint;
    private CacheBuilder.Loader loader;

    public Actuator(CacheBuilder cache) {
        this.cache = cache;
    }

    public void init() {
        if (this.isLogWrite())
            log.debug("Actuator.init initializing");
        try {
            this.makeStoragePlugin();
            if (this.storage != null)
                this.internalSetItems(this.internalLoadStorage());
            if (this.cache.loader() == null)
                return;

            if (this.isLogWrite())
                log.debug("Actuator.init.loader initializing");
            var loader = this.cache.loader();
            var checkPoint = this.checkPoint();
            if (!loader.available(checkPoint))
                return;
            if (this.isLogWrite())
                log.debug("Actuator.init.loader started, checkPoint: [{}]", checkPoint);
            if (!loader.load(cache)) {
                if (this.isLogWrite())
                    log.debug("Actuator.init.loader fail");
                return;
            }
            if (this.isLogWrite())
                log.debug("Actuator.init.loader checkpoint update: [{}]", loader.checkPoint());
            this.setCheckPoint(loader.checkPoint());
            if (this.isLogWrite())
                log.debug("Actuator.init.loader finished, new checkPoint: [{}]", checkPoint);
        } finally {
            if (this.isLogWrite())
                log.debug("Actuator.init initialized");
        }
    }

    public final CacheBuilder.Loader loader() {
        return this.loader;
    }

    public void setLoader(final CacheBuilder.Loader loader) {
        this.loader = loader;
    }

    public Plugin storage() {
        return this.storage;
    }

    public void setCheckPoint(Object checkPoint) {
        this.checkPoint = checkPoint;
    }

    public final List<Object> items() {
        if (this.isLogWrite())
            log.debug("loading storage items, storage: [{}]", storageClassName());
        if (storage == null)
            return this.items;
        if (this.items.isEmpty())
            this.items.addAll(storage.items());
        return this.items;
    }

    public final int itemsCount() {
        return this.storage == null ? this.items.size() : this.storage.itemsCount();
    }

    public void clear() {
        this.checkPoint = null;
        synchronized (this.itemsMap) {
            this.itemsMap.clear();
            this.items.clear();
            if (this.storage != null) {
                this.storage.clear();
                this.storage.prepare();
            }
        }
    }

    public final Object checkPoint() {
        return this.checkPoint;
    }

    public final CacheBuilder cache() {
        return this.cache;
    }

    public void setItems(final List<Object> items) {
        if (this.storage != null) {
            if (this.isLogWrite())
                log.debug("appending storage items");
            items.forEach(this::put);
        }
        this.internalSetItems(items);
    }

    public void put(CacheBuilder.CacheItem item) {
        if (item == null) {
            if (this.isLogWrite())
                log.debug("CacheBuilder.CacheItem is null");
            return;
        }
        synchronized (this.items) {
            if (!items.contains(item))
                items.add(item);
            this.itemsMap.put(item.id(), item);
            if (this.storage != null)
                this.storage.put(item);
        }
    }

    public void put(Object data) {
        var id = itemIdMaker(data);
        if (id == null) {
            log.error("Invalid item id from data, Object \n " + ObjectUtil.toString(data));
            throw new FrameworkException("Invalid data id");
        }
        this.put(new CacheBuilder.CacheItem(id, data));
    }

    public final CacheBuilder.CacheItem get(UUID id) {
        if (id == null)
            return null;
        var item = this.itemsMap.get(id);
        if (item == null && this.storage != null)
            this.put(this.storage.get(id));
        return item;
    }

    public boolean remove(final UUID id) {
        var item = this.get(id);
        if (item == null)
            return false;
        synchronized (this.items) {
            this.itemsMap.remove(item.id());
            this.items.remove(item);
        }
        if (this.storage != null)
            this.storage.remove(id);
        return true;
    }

    private UUID itemIdMaker(Object o) {
        return this.cache.key().dataId(o);
    }

    private boolean isLogWrite() {
        return this.cache.setting().isLogWrite();
    }

    private List<Object> internalLoadStorage() {
        if (this.storage == null)
            return new ArrayList<>();
        if (this.isLogWrite())
            log.debug("loading storage items");
        return List.of(this.storage.items());
    }

    private void internalSetItems(final List<Object> items) {
        if (this.isLogWrite())
            log.debug("loading memory items");
        this.clear();
        if (items == null)
            return;
        synchronized (this.items) {
            for (var o : items) {
                if (o instanceof CacheBuilder.CacheItem item) {
                    this.items.add(item);
                    this.itemsMap.put(item.id(), item);
                    continue;
                }
                this.put(o);
            }
        }
    }

    private void makeStoragePlugin() {
        if (this.isLogWrite())
            log.debug("makeStoragePlugin started");
        var pluginName = this.cache.setting().storage().getType();
        if (pluginName == null || pluginName.isEmpty()) {
            if (this.isLogWrite())
                log.debug("makeStoragePlugin is empty, skipped");
            this.storage = null;
            return;
        }
        Class<?> pluginClass;
        try {
            pluginClass = Class.forName(pluginName);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new FrameworkException("Storage plugin not found: " + pluginName);
        }

        if (this.isLogWrite())
            log.debug("makeStoragePlugin, storage plugin found: [{}]", pluginClass.getName());

        if (this.storage != null && pluginClass.equals(this.storage.getClass())) {
            if (this.isLogWrite())
                log.debug("makeStoragePlugin, storage plugin has been created: [{}]", pluginClass.getName());
            return;
        }
        if (this.isLogWrite())
            log.debug("creating storage plugin instance: {}", pluginName);
        this.storage = ObjectUtil.create(pluginClass);
        if (this.storage == null)
            throw new FrameworkException("No create instance of storage plugin: " + pluginClass.getName());
        this.storage.setCache(this.cache);
        this.storage.prepare();
        if (this.isLogWrite())
            log.debug("successful on create storage plugin instance: {}", pluginName);
    }

    private String storageClassName() {
        return storage == null ? "" : storage.getClass().toString();
    }

}