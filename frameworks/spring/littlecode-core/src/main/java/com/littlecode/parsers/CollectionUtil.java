package com.littlecode.parsers;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CollectionUtil<T> {
    private final Object target;
    private Filter<T> filter;

    public CollectionUtil(List<T> target) {
        if (target == null)
            throw ExceptionBuilder.ofNullPointer("Target is null");
        this.target = target;
    }

    public CollectionUtil(Map<?, T> target) {
        if (target == null)
            throw ExceptionBuilder.ofNullPointer("Target is null");
        this.target = target.values();
    }

    public CollectionUtil filter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public List<Object> toObjectList() {
        if (target instanceof List value)
            return value;
        return new ArrayList<>();
    }

    public T asItem() {
        var list = this.toObjectList();
        for (var v : list) {
            T item = (T) v;
            var go = (this.filter == null || this.filter.matched(item));
            if (go)
                return item;
        }
        return null;
    }

    public Map<String, T> asMap(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty() || this.target == null)
            return new HashMap<>();

        var listObject = this.toObjectList();
        if (listObject.isEmpty())
            return new HashMap<>();

        var oValues = ObjectValueUtil.of(listObject.get(0));

        final var field = oValues.getField(fieldName);
        if (field == null)
            return new HashMap<>();

        Map<String, T> __return = new HashMap<>();
        for (var v : listObject) {
            T item = (T) v;

            var go = (this.filter == null || this.filter.matched(item));

            if (go) {
                var k = oValues
                        .target(v)
                        .asString(field);
                if (item != null)
                    __return.put(k, item);
            }

        }
        return __return;
    }

    public List<T> asList() {
        var list = this.toObjectList();
        List<T> __return = new ArrayList<>();
        for (var v : list) {
            T item = (T) v;
            if (!__return.contains(item)) {
                var go = (this.filter == null || this.filter.matched(item));
                if (go)
                    __return.add(item);
            }
        }
        return __return;
    }

    @FunctionalInterface
    public interface Filter<O> {
        boolean matched(O item);
    }

    public static <T> List<T> cloneList(Class<?> aClass, List<T> src) {
        var list = new ArrayList<T>();
        for (var v : src){
            var item=ObjectUtil.createFromObject(v.getClass(), v);
            if(item==null)
                throw ExceptionBuilder.ofNullPointer("Invalid clone item[%s], check constructores", v.getClass());
            list.add((T)item);
        }
        return list;
    }

}