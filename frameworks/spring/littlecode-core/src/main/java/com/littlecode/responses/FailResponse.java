package com.littlecode.responses;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FailResponse {
    private Object id;
    private LocalDateTime dt;
    private Class<?> className;
    private Object classMethod;
    private Object type;
    private Object relatedObject;
    private Object message;
    private Class<?> exception;

    public static Object of(List<Object> objects) {
        if (objects == null || objects.isEmpty())
            return null;
        List<Object> list = new ArrayList<>();
        for (var o : objects) {
            list.add(
                    o == null
                            ? Map.of("null", "")
                            : Map.of(o.getClass().getSimpleName(), of(o))
            );
        }
        return list;
    }

    public static Object of(String name, Object o) {
        return Map.of(name, of(o));
    }

    public static Object of(Object o) {
        return Map.of(
                "class", o == null
                        ? "null"
                        : o.getClass().getName(),
                "object", o == null
                        ? "null"
                        : o
        );
    }

    public LocalDateTime getDt() {
        return this.dt == null ? LocalDateTime.now() : this.dt;
    }
}
