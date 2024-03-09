package com.littlecode.cache;

import com.littlecode.cache.plugins.impl.DiskPlugin;
import com.littlecode.cache.plugins.impl.MemCachedPlugin;
import com.littlecode.cache.plugins.impl.PostgresPlugin;
import com.littlecode.cache.privates.Actuator;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.util.*;

@Slf4j
public class CacheBuilder {
    private final Setting setting = new Setting(this);
    private final Actuator actuator = new Actuator(this);
    private final Key key = new Key();
    private String name;

    public void load() {
        this.actuator.init();
    }

    public final Setting setting() {
        return this.setting;
    }

    public final String name() {
        if (this.name == null)
            this.name = "";
        return this.name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = (name == null || name.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : name.trim();
    }

    public final Key key() {
        return this.key;
    }

    public void setKey(Key key) {
        this.key.clear().add(key.getKeys());
    }

    @SuppressWarnings("unused")
    public void setKey(String[] keys) {
        this.setKey(Key.of(keys));
    }

    @SuppressWarnings("unused")
    public void setKey(List<String> newKeys) {
        this.key.clear();
        if (newKeys != null) {
            newKeys.forEach(s -> {
                if (s != null && !s.trim().isEmpty())
                    this.key.add(s);
            });
        }
    }

    public final Actuator actuator() {
        return actuator;
    }

    public final Loader loader() {
        return this.actuator.loader();
    }

    public void setLoader(final Loader loader) {
        this.actuator.setLoader(loader);
    }

    @SuppressWarnings("unused")
    public void setLoader(Class<?> loader) {
        this.actuator.setLoader(ObjectUtil.create(loader));
    }

    public Seeker.SeekerBuilder seeker() {
        var builder = Seeker.builder();
        builder.cache(this);
        return builder;
    }

    public void clear() {
        this.actuator().clear();
    }

    public List<Object> items() {
        return this.actuator().items();
    }

    public int itemsCount() {
        return this.actuator().itemsCount();
    }

    public void setItems(final List<Object> items) {
        this.actuator().setItems(items);
    }

    public void put(final CacheBuilder.CacheItem item) {
        this.actuator().put(item);
    }

    public CacheBuilder.CacheItem get(UUID id) {
        return this.actuator().get(id);
    }

    public boolean remove(UUID item) {
        return this.actuator().remove(item);
    }

    public interface Loader {
        boolean available(final Object checkPoint);

        boolean load(CacheBuilder cache);

        Object checkPoint();
    }

    public static class CacheItem {
        private static final String FIELD_ID = "id";
        private static final String FIELD_TYPE = "type";
        private static final String FIELD_DATA = "data";
        private final UUID id;
        private final Class<?> type;
        private final String data;
        private Object dataPvt;

        @SuppressWarnings("unused")
        public CacheItem() {
            this.id = null;
            this.type = null;
            this.data = null;
        }

        public CacheItem(final UUID id, final String data, final Class<?> type) {
            this.id = id;
            this.data = data;
            this.type = type;
            this.dataPvt = null;
        }

        public CacheItem(final UUID id, final Object data) {
            this.id = id;
            if (data == null) {
                this.type = Object.class;
                this.data = null;
            } else {
                this.type = data.getClass();
                this.data = ObjectUtil.toString(data);
            }
            this.dataPvt = data;
        }

        public static CacheItem from(String values) {
            try {
                var map = ObjectUtil.toMapOfString(values);
                var id = UUID.fromString(map.get(FIELD_ID));
                var data = map.get(FIELD_DATA);
                var type = Class.forName(map.get(FIELD_TYPE));
                return new CacheItem(id, data, type);
            } catch (ClassNotFoundException e) {
                throw new FrameworkException(e);
            }
        }

        public final UUID id() {
            return this.id;
        }

        public final Object data() {
            if (this.dataPvt != null)
                return this.dataPvt;
            if (this.data == null || this.data.isEmpty())
                return null;
            this.dataPvt = ObjectUtil.createFromString(this.type, this.data);
            return this.dataPvt;
        }

        @SuppressWarnings("unused")
        public final Class<?> type() {
            return this.type;
        }

        public <T> T as(Class<T> aClass) {
            var data = this.data();
            return data == null ? null : aClass.cast(data);
        }

        @SuppressWarnings("unused")
        public <T> T asCopy(Class<T> aClass) {
            var data = this.data();
            if (data == null)
                return null;
            var mapper = new ModelMapper();
            return mapper.map(data, aClass);
        }

        public String toString() {
            Map<String, String> map = Map.of(
                    FIELD_ID, this.id.toString(),
                    FIELD_DATA, this.data,
                    FIELD_TYPE, this.type.getName()
            );
            return ObjectUtil.toString(map);
        }
    }

    @Getter
    public static class Key {
        private final List<String> keys = new ArrayList<>();

        public Key() {
        }

        @SuppressWarnings("unused")
        public Key(String keyName) {
            add(keyName);
        }

        public static UUID dataId(Key keyObject, Object data) {
            if (keyObject == null)
                throw new FrameworkException("Invalid key Object");
            if (data == null)
                throw new FrameworkException("Invalid data Object");
            var fieldsMap = ObjectUtil.toFieldsMap(data);
            if (fieldsMap.isEmpty())
                throw new FrameworkException("Invalid field objects");
            if (keyObject.keys.isEmpty())
                return HashUtil.toMd5Uuid(data);
            Map<String, Object> mapValues = new HashMap<>();
            keyObject.keys
                    .forEach(fieldName -> {
                        try {
                            var field = fieldsMap.get(fieldName);
                            if (field == null)
                                throw new FrameworkException(String.format("Field[%s] not found", fieldName));
                            mapValues.put(field.getName(), field.get(data));
                        } catch (IllegalAccessException e) {
                            throw new FrameworkException(e);
                        }
                    });
            return HashUtil.toMd5Uuid(mapValues);
        }

        public static Key of() {
            return new Key();
        }

        public static Key of(List<String> keys) {
            var key = of();
            keys.forEach(key::add);
            return key;
        }

        public static Key of(String[] keys) {
            return of(List.of(keys));
        }

        public static Key of(String keyName) {
            return of()
                    .add(keyName);
        }

        public static Key of(String k1, String k2) {
            return of()
                    .add(k1)
                    .add(k2);
        }

        public static Key of(String k1, String k2, String k3) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3);
        }

        public static Key of(String k1, String k2, String k3, String k4) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4);
        }

        public static Key of(String k1, String k2, String k3, String k4, String k5) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4)
                    .add(k5);
        }

        public static Key of(String k1, String k2, String k3, String k4, String k5, String k6) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4)
                    .add(k5)
                    .add(k6);
        }

        public static Key of(String k1, String k2, String k3, String k4, String k5, String k6, String k7) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4)
                    .add(k5)
                    .add(k6)
                    .add(k7);
        }

        public static Key of(String k1, String k2, String k3, String k4, String k5, String k6, String k7, String k8) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4)
                    .add(k5)
                    .add(k6)
                    .add(k7)
                    .add(k8);
        }

        public static Key of(String k1, String k2, String k3, String k4, String k5, String k6, String k7, String k8, String k9) {
            return of()
                    .add(k1)
                    .add(k2)
                    .add(k3)
                    .add(k4)
                    .add(k5)
                    .add(k6)
                    .add(k7)
                    .add(k8)
                    .add(k9);
        }

        public Key clear() {
            this.keys.clear();
            return this;
        }

        public boolean isEmpty() {
            return this.keys.isEmpty();
        }

        public UUID dataId(Object data) {
            return dataId(this, data);
        }

        public Key add(String keyName) {
            if (keyName != null && !keyName.trim().isEmpty())
                this.keys.add(keyName);
            return this;
        }

        public Key add(List<String> keyNames) {
            if (keyNames == null || keyNames.isEmpty())
                return this;
            keyNames.forEach(this::add);
            return this;
        }

        public Key add(String[] keyNames) {
            return this.add(List.of(keyNames));
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Seeker {
        private final List<Result> results = new ArrayList<>();
        private CacheBuilder cache;
        private MethodItem onPeek;
        private MethodItem onMatch;
        private MethodVoid onFinished;

        @SuppressWarnings("unused")
        public final List<Result> seek() {
            this.cache.load();
            return this.clear().internalSeek().results();
        }

        public final List<Result> results() {
            return this.results;
        }

        public Seeker clear() {
            this.results.clear();
            return this;
        }

        private Seeker internalSeek() {
            Map<UUID, Result> resultMap = new HashMap<>();
            cache
                    .actuator()
                    .items()
                    .forEach((i) ->
                    {
                        var item = (CacheItem) i;
                        var onPeek = (this.onPeek == null || this.onPeek.apply(item));
                        if (!onPeek)
                            return;

                        var onMatch = (this.onMatch == null || this.onMatch.apply(item));
                        if (!onMatch)
                            return;

                        if (!resultMap.containsKey(item.id())) {
                            var itemResult = Result.from(item);
                            resultMap.put(item.id(), itemResult);
                            this.results.add(itemResult);
                            return;
                        }
                        resultMap.get(item.id()).incMatch();
                    });
            if (this.onFinished != null)
                this.onFinished.apply();
            return this;
        }

        @FunctionalInterface
        public interface MethodVoid {
            void apply();
        }

        @FunctionalInterface
        public interface MethodItem {
            boolean apply(CacheItem cacheItem);
        }

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Result {
            private int matches;
            private CacheItem cacheItem;

            public static Result from(CacheItem item) {
                return Result
                        .builder()
                        .cacheItem(item)
                        .matches(1)
                        .build();
            }

            @SuppressWarnings("unused")
            public int matches() {
                return matches;
            }

            public final CacheItem cacheItem() {
                return cacheItem;
            }

            public void incMatch() {
                ++matches;
            }
        }
    }

    @Builder
    @AllArgsConstructor
    public static class Setting {
        private final Expiration expiration = new Expiration(this);
        private final Storage storage = new Storage(this);
        @Getter
        @Setter
        private boolean logWrite;
        private CacheBuilder cache;

        public Setting(CacheBuilder cache) {
            this.cache = cache;
        }

        public Expiration expiration() {
            return this.expiration;
        }

        public Storage storage() {
            return this.storage;
        }

        @Builder
        @Getter
        @Setter
        @AllArgsConstructor
        public static class Expiration {
            private Setting setting;
            private int onlyStorage;
            private int toDiscard;

            public Expiration(Setting setting) {
                this.setting = setting;
            }
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Storage {
        private boolean autoClear;
        private Setting setting;
        private String type;
        private String uri;
        private String protocol;
        private String hostName;
        private String userName;
        private String password;
        private String token;
        private String local;

        public Storage(Setting setting) {
            this.setting = setting;
        }

        public String getType() {
            return this.type == null ? null : type.trim();
        }

        public void setType(String type) {
            this.setType(CacheBuilder.StorageType.from(type));
        }

        public void setType(Class<?> type) {
            this.type = type == null ? null : type.getName();
            if (this.setting != null && this.setting.cache != null)
                this.setting.cache.actuator.init();
        }
    }

    public static class StorageType {
        public static final Class<?> Disk;
        public static final Class<?> MemCached;
        public static final Class<?> Postgres;

        static {
            Disk = DiskPlugin.class;
            MemCached = MemCachedPlugin.class;
            Postgres = PostgresPlugin.class;
        }

        public static List<Class<?>> list() {
            return List.of(Disk, MemCached, Postgres);
        }

        public static List<String> listNames() {
            List<String> list = new ArrayList<>(List.of(""));
            list().forEach(plugin ->
                    list.add(plugin.getName())
            );
            return list;
        }

        public static Class<?> from(String storageName) {
            if (storageName == null || storageName.trim().isEmpty())
                return null;
            storageName = storageName.trim();
            for (var storage : list()) {
                if (storageName.equalsIgnoreCase(storage.getName()))
                    return storage;
            }
            try {
                return Class.forName(storageName);
            } catch (Exception e) {
                throw new FrameworkException(e);
            }
        }
    }
}
