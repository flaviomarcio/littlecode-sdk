package com.littlecode.parsers;

import com.littlecode.exceptions.FrameworkException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CollectionUtil {

    public static <K, V> Map<?, V> toMap(String fieldName, List<V> values) {
        if (values == null || values.isEmpty())
            return new HashMap<>();
        Map<K, V> __return = new HashMap<>();
        Class<?> keyFieldClass = null;
        Field keyField = null;
        for (var value : values) {
            try {
                if (keyField != null && !value.getClass().equals(keyFieldClass)) {
                    keyField = null;
                    keyFieldClass = null;
                }

                if (keyField == null) {
                    keyField = ObjectUtil.toFieldByName(value.getClass(),fieldName);
                    if (keyField == null)
                        throw new FrameworkException("Invalid key conversion");
                    keyFieldClass = value.getClass();
                }
                var o = keyField.get(value);
                @SuppressWarnings("unchecked")
                var k = (K) o;
                //noinspection ConstantValue
                if (k == null && o != null)
                    throw new FrameworkException("Invalid key conversion");
                __return.put(k, value);
            } catch (IllegalAccessException e) {
                throw new FrameworkException(e);
            }
        }
        return __return;
    }

    public static <V> List<V> toList(Map<?, V> values) {
        List<V> __return = new ArrayList<>();
        for (Map.Entry<?, V> entry : values.entrySet()) {
            V o = entry.getValue();
            if (!__return.contains(o))
                __return.add(o);
        }
        return __return;
    }

}