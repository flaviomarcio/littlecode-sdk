package com.littlecode.cache.plugins.impl;

import com.littlecode.cache.CacheBuilder;
import com.littlecode.cache.plugins.PluginBase;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.PathUtil;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class DiskPlugin extends PluginBase {
    private Path localCache;
    private String cacheName;
    private UUID localName;

    public void prepare() {
        if (!PrimitiveUtil.isEmpty(this.cacheName)) {
            if (!PrimitiveUtil.isEmpty(this.localCache)) {
                var path = localCache.toFile();
                if (path.isDirectory() && path.exists())//if exists
                    return;
            }
            this.localName = HashUtil.toMd5Uuid(localName);
        } else if (PrimitiveUtil.isEmpty(this.localName)) {
            this.localName = UUID.randomUUID();
        }

        var directory = PathUtil.tempFile(this.localName.toString()).toFile();
        if (!directory.exists() && !directory.mkdir())
            throw new FrameworkException("No create cache dir, cacheDir: " + this.localCache);
        if (!directory.isDirectory())
            throw new FrameworkException("cache dir is not a directory, cacheDir: " + this.localCache);
        this.localCache = directory.toPath();
        this.cacheName = this.cache().name();
    }

    @Override
    public void clear() {
        PathUtil.rmDir(this.localCache);
        this.localCache = null;
        this.prepare();
    }

    @Override
    public List<CacheBuilder.CacheItem> loadItems() {
        if (this.isLogWrite())
            log.debug("save cache local: {}", this.localCache);
        var listFiles = PathUtil.listFiles(this.localCache);
        if (listFiles.isEmpty())
            return new ArrayList<>();
        List<CacheBuilder.CacheItem> __return = new ArrayList<>();
        listFiles.forEach(file -> {
            var id = HashUtil.toUuid(file.getName());
            if (id == null) {
                log.error("Invalid cache file: id is null, {}", file.getAbsolutePath());
                return;
            }
            __return.add(this.get(id));
        });
        return __return;
    }

    @Override
    public final List<CacheBuilder.CacheItem> items() {
        return this.loadItems();
    }

    @Override
    public int itemsCount() {
        return PathUtil.countFiles(this.localCache);
    }

    @Override
    public void setItems(final List<CacheBuilder.CacheItem> items) {
        if (this.isLogWrite())
            log.debug("save cache files");
        this.clear();
        items.forEach(this::put);
    }

    @Override
    public void put(final CacheBuilder.CacheItem item) {
        try {
            var itemFile = this.itemFile(item.id()).toFile();
            if (this.isLogWrite())
                log.debug("save cache file: {}", itemFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(itemFile));
            writer.write(item.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    @Override
    public final CacheBuilder.CacheItem get(UUID id) {
        try {
            var itemFile = this.itemFile(id).toFile();
            if (this.isLogWrite())
                log.debug("load cache file: {}", itemFile);
            FileInputStream inputStream = new FileInputStream(itemFile);
            byte[] body = new byte[(int) itemFile.length()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(body);
            inputStream.close();
            return CacheBuilder.CacheItem.from(new String(body));
        } catch (IOException e) {
            if (isLogWrite())
                log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean remove(UUID id) {
        var file = itemFile(id).toFile();
        return file.delete();
    }

    private Path itemFile(UUID id) {
        return PathUtil.fileName(this.localCache, id);
    }

}
