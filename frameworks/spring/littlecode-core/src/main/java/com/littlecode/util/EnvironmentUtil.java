package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.parsers.ExceptionBuilder;
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
        this.environment = environment;
    }

    private String envValue(String env) {
        if (environment == null)
            return null;
        try {
            var value=environment.getProperty(env);
            return value==null?null:value.trim();
        } catch (Exception e) {
            log.debug("fail: {}", e.getMessage());
            return null;
        }
    }

    public String asString(String env) {
        return this.asString(env, null);
    }

    public String asString(String env, String defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue == null ? "" : defaultValue;
        return eValue;
    }

    public boolean asBool(String env) {
        return this.asBool(env, false);
    }

    public boolean asBool(String env, boolean defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toBool(eValue);
    }

    public double asDouble(String env) {
        return this.asDouble(env, 0);
    }

    public double asDouble(String env, double defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toDouble(eValue);
    }

    public int asInt(String env) {
        return this.asInt(env, 0);
    }

    public int asInt(String env, int defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toInt(eValue);
    }

    public long asLong(String env) {
        return this.asLong(env, 0);
    }

    public long asLong(String env, long defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toInt(eValue);
    }

    public LocalDate asDate(String env) {
        return this.asDate(env, null);
    }

    public LocalDate asDate(String env, LocalDate defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toDate(eValue);
    }

    public LocalTime asTime(String env) {
        return this.asTime(env, null);
    }

    public LocalTime asTime(String env, LocalTime defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toTime(eValue);
    }

    public LocalDateTime asDateTime(String env) {
        return this.asDateTime(env, null);
    }

    public LocalDateTime asDateTime(String env, LocalDateTime defaultValue) {
        var eValue = envValue(env);
        if (eValue == null)
            return defaultValue;
        return PrimitiveUtil.toDateTime(eValue);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass) {
        return asEnums(env, enumClass, null);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass, List<T> defaultValue) {
        if (enumClass == null)
            throw ExceptionBuilder.ofFrameWork("Invalid enum: enum is null");
        if (!enumClass.isEnum())
            throw ExceptionBuilder.ofFrameWork("Invalid enum: %s is not enum type", enumClass.getName());

        var enumList = enumClass.getEnumConstants();

        List<T> __return = new ArrayList<>();
        try {
            var eValue=envValue(env);
            if(eValue==null)
                return defaultValue==null?new ArrayList<>():defaultValue;
            var values = eValue.split(",");
            for (String s : values) {
                if (s == null || s.trim().isEmpty())
                    continue;
                for (var e : enumList) {
                    var eName = e.toString();
                    if (!eName.equalsIgnoreCase(s))
                        continue;
                    //noinspection unchecked
                    __return.add((T) e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return !__return.isEmpty()
                ? __return
                :
                defaultValue == null
                        ? new ArrayList<>()
                        : defaultValue;
    }

    public List<String> asListOfString(String env) {
        return this.asListOfString(env,null);
    }

    public List<String> asListOfString(String env, List<String> defaultValue) {
        try {
            var eValue = envValue(env);
            if (eValue == null)
                return defaultValue==null?new ArrayList<>():defaultValue;
            var values = List.of(eValue.split(","));
            List<String> out = new ArrayList<>();
            values.forEach(s -> {
                if (s != null && !s.trim().isEmpty())
                    out.add(s);
            });
            if(out.isEmpty())
                return defaultValue==null?new ArrayList<>():defaultValue;
            return out;
        } catch (Exception e) {
            log.error(e.getMessage());
            return defaultValue==null?new ArrayList<>():defaultValue;
        }
    }

    public List<Long> asListOfLong(String env) {
        return this.asListOfLong(env,null);
    }

    public List<Long> asListOfLong(String env, List<Long> defaultValue) {
        try {
            var eValue = envValue(env);
            if (eValue == null)
                return defaultValue==null?new ArrayList<>():defaultValue;
            var values = List.of(eValue.split(","));
            List<Long> out = new ArrayList<>();
            values.forEach(s -> {
                if (s != null && !s.trim().isEmpty()){
                    var value=PrimitiveUtil.toLong(s);
                    if(String.valueOf(value).equals(s))
                        out.add(value);
                }
            });
            if(out.isEmpty())
                return defaultValue==null?new ArrayList<>():defaultValue;
            return out;
        } catch (Exception e) {
            log.error(e.getMessage());
            return defaultValue==null?new ArrayList<>():defaultValue;
        }
    }

    public List<Integer> asListOfInt(String env) {
        return this.asListOfInt(env,null);
    }

    public List<Integer> asListOfInt(String env, List<Integer> defaultValue) {
        try {
            var eValue = envValue(env);
            if (eValue == null)
                return defaultValue==null?new ArrayList<>():defaultValue;
            var values = List.of(eValue.split(","));
            List<Integer> out = new ArrayList<>();
            values.forEach(s -> {
                if (s != null && !s.trim().isEmpty()){
                    var value=PrimitiveUtil.toInt(s);
                    if(String.valueOf(value).equals(s))
                        out.add(value);
                }
            });
            if(out.isEmpty())
                return defaultValue==null?new ArrayList<>():defaultValue;
            return out;
        } catch (Exception e) {
            log.error(e.getMessage());
            return defaultValue==null?new ArrayList<>():defaultValue;
        }
    }

}

