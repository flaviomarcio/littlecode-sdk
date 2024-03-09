package com.littlecode.cache.plugins;

import com.littlecode.cache.CacheBuilder;
import com.littlecode.cache.privates.Actuator;

import java.util.List;
import java.util.UUID;

public interface Plugin {

    boolean isLogWrite();

    CacheBuilder cache();

    void setCache(CacheBuilder cache);

    Actuator actuator();

    CacheBuilder.Setting setting();

    void prepare();

    void clear();

    List<CacheBuilder.CacheItem> loadItems();

    List<CacheBuilder.CacheItem> items();

    int itemsCount();

    void setItems(final List<CacheBuilder.CacheItem> items);

    void put(CacheBuilder.CacheItem item);

    CacheBuilder.CacheItem get(UUID id);

    boolean remove(UUID id);
}
