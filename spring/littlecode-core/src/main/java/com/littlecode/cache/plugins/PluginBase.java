package com.littlecode.cache.plugins;

import com.littlecode.cache.CacheBuilder;
import com.littlecode.cache.privates.Actuator;
import com.littlecode.exceptions.FrameworkException;

import java.util.List;
import java.util.UUID;

public class PluginBase implements Plugin {
    private static final String NO_IMPLEMENTED = "No implemented";
    private CacheBuilder cache;

    @Override
    public boolean isLogWrite() {
        return this.cache.setting().isLogWrite();
    }

    @Override
    public final CacheBuilder cache() {
        return this.cache;
    }

    @Override
    public void setCache(final CacheBuilder cache) {
        this.cache = cache;
    }

    @Override
    public final Actuator actuator() {
        return this.cache.actuator();
    }

    @Override
    public final CacheBuilder.Setting setting() {
        return this.cache.setting();
    }

    @Override
    public void prepare() {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public void clear() {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public List<CacheBuilder.CacheItem> loadItems() {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public List<CacheBuilder.CacheItem> items() {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public int itemsCount() {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public void setItems(final List<CacheBuilder.CacheItem> items) {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public void put(final CacheBuilder.CacheItem item) {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public CacheBuilder.CacheItem get(UUID id) {
        throw new FrameworkException(NO_IMPLEMENTED);
    }

    @Override
    public boolean remove(UUID id) {
        throw new FrameworkException(NO_IMPLEMENTED);
    }
}

