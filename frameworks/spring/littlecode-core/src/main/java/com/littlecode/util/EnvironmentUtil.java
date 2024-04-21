package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class EnvironmentUtil {
    private final Environment environment;

    public EnvironmentUtil() {
        this.environment = UtilCoreConfig.getEnvironment();
    }

    public EnvironmentUtil(Environment environment) {
        if (environment == null)
            throw new FrameworkException("environment is null");
        this.environment = environment;
    }

    public String envValue(String env) {
        if (env != null && !env.isEmpty()) {
            try {
                var eValue = environment.getProperty(env);
                return eValue == null || eValue.isEmpty()
                        ? null
                        : eValue;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public String asString(String env) {
        return this.asString(env, null);
    }

    public String asString(String env, String defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue == null ? null : defaultValue.trim()
                : eValue.trim();
    }

    public boolean asBool(String env) {
        return this.asBool(env, false);
    }

    public boolean asBool(String env, boolean defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue :
                PrimitiveUtil.toBool(eValue);
    }

    public double asDouble(String env) {
        return this.asDouble(env, 0);
    }

    public double asDouble(String env, double defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toDouble(eValue);
    }

    public int asInt(String env) {
        return this.asInt(env, 0);
    }

    public int asInt(String env, int defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toInt(eValue);
    }

    public long asLong(String env) {
        return this.asLong(env, 0);
    }

    public long asLong(String env, long defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toLong(eValue);
    }

    public LocalDate asDate(String env) {
        return this.asDate(env, null);
    }

    public LocalDate asDate(String env, LocalDate defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toDate(eValue);
    }

    public LocalTime asTime(String env) {
        return this.asTime(env, null);
    }

    public LocalTime asTime(String env, LocalTime defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toTime(eValue);
    }

    public LocalDateTime asDateTime(String env) {
        return this.asDateTime(env, null);
    }

    public LocalDateTime asDateTime(String env, LocalDateTime defaultValue) {
        var eValue = envValue(env);
        return (eValue == null)
                ? defaultValue
                : PrimitiveUtil.toDateTime(eValue);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass) {
        return asEnums(env, enumClass, null);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass, List<T> defaultValue) {
        if (enumClass != null && enumClass.isEnum()) {
            var enumList = enumClass.getEnumConstants();
            List<T> __return = new ArrayList<>();
            var eValue=envValue(env);
            if (eValue != null) {
                var values = eValue.split(",");
                for (String s : values) {
                    if (s != null && !s.trim().isEmpty()) {
                        for (var e : enumList) {
                            var eName = e.toString();
                            if (eName.equalsIgnoreCase(s))
                                __return.add((T) e);
                        }
                    }
                }
                if (!__return.isEmpty())
                    return __return;
            }
        }
        return defaultValue == null ? new ArrayList<>() : defaultValue;
    }

    public List<String> asListOfString(String env, List<String> defaultValue) {
        var eValue = envValue(env);
        if (eValue != null && !eValue.trim().isEmpty()) {
            var values = List.of(eValue.split(","));
            List<String> out = new ArrayList<>();
            for (var s : values) {
                if (s != null && !s.trim().isEmpty())
                    out.add(s);
            }
            return out;
        }
        return defaultValue == null ? new ArrayList<>() : defaultValue;
    }

    public List<String> asListOfString(String env) {
        return this.asListOfString(env, null);
    }

    public List<Long> asListOfLong(String env) {
        return this.asListOfLong(env,null);
    }

    public List<Long> asListOfLong(String env, List<Long> defaultValue) {
        var eValue = envValue(env);
        if (eValue != null && !eValue.trim().isEmpty()) {
            var values = List.of(eValue.split(","));
            List<Long> out = new ArrayList<>();
            for (var s : values)
                out.add(PrimitiveUtil.toLong(s));
            return out;
        }
        return defaultValue == null ? new ArrayList<>() : defaultValue;
    }

    public List<Integer> asListOfInt(String env, List<Integer> defaultValue) {
        var eValue = envValue(env);
        if (eValue != null && !eValue.trim().isEmpty()) {
            var values = List.of(eValue.split(","));
            List<Integer> out = new ArrayList<>();
            for (String s : values) {
                if (s != null && !s.trim().isEmpty())
                    out.add(PrimitiveUtil.toInt(s));
            }
            return out;
        }
        return defaultValue == null ? new ArrayList<>() : defaultValue;
    }

    public List<Integer> asListOfInt(String env) {
        return this.asListOfInt(env, null);
    }

}

