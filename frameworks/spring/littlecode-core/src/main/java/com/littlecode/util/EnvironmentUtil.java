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
        if(this.environment == null)
            throw ExceptionBuilder.ofFrameWork("environment is null");
    }

    public EnvironmentUtil(Environment environment) {
        this.environment = environment;
        if(this.environment == null)
            throw ExceptionBuilder.ofFrameWork("environment is null");
    }

    public String envValue(String env, String defaultValue) {
        return
                (environment.containsProperty(env))
                        ? environment.getProperty(env).trim()
                        : defaultValue;
    }

    public String envValue(String env) {
        return this.envValue(env, null);
    }

    public String asString(String env) {
        return this.asString(env, null);
    }

    public String asString(String env, String defaultValue) {
        var eValue = envValue(env);
        return (eValue != null)
                ? eValue
                : defaultValue == null ? "" : defaultValue;
    }

    public boolean asBool(String env) {
        return this.asBool(env, false);
    }

    public boolean asBool(String env, boolean defaultValue) {
        var eValue = envValue(env);
        return (PrimitiveUtil.toBool(eValue)) || defaultValue;
    }

    public double asDouble(String env) {
        return this.asDouble(env, 0);
    }

    public double asDouble(String env, double defaultValue) {
        return PrimitiveUtil.toDouble(envValue(env, String.valueOf(defaultValue)));
    }

    public int asInt(String env) {
        return this.asInt(env, 0);
    }

    public int asInt(String env, int defaultValue) {
        return PrimitiveUtil.toInt(envValue(env, String.valueOf(defaultValue)));
    }

    public long asLong(String env) {
        return this.asLong(env, 0);
    }

    public long asLong(String env, long defaultValue) {
        return PrimitiveUtil.toLong(envValue(env, String.valueOf(defaultValue)));
    }

    public LocalDate asDate(String env) {
        return PrimitiveUtil.toDate(envValue(env));
    }

    public LocalDate asDate(String env, LocalDate defaultValue) {
        return PrimitiveUtil.toDate(envValue(env, PrimitiveUtil.toString(defaultValue)));
    }

    public LocalTime asTime(String env) {
        return PrimitiveUtil.toTime(envValue(env));
    }

    public LocalTime asTime(String env, LocalTime defaultValue) {
        return PrimitiveUtil.toTime(envValue(env, PrimitiveUtil.toString(defaultValue)));
    }

    public LocalDateTime asDateTime(String env) {
        return PrimitiveUtil.toDateTime(envValue(env));
    }

    public LocalDateTime asDateTime(String env, LocalDateTime defaultValue) {
        return PrimitiveUtil.toDateTime(envValue(env, PrimitiveUtil.toString(defaultValue)));
    }

    public <T> T asEnum(String env, Class<T> enumClass, T defaultValue) {
        if (enumClass != null && enumClass.isEnum()){
            var enumList = enumClass.getEnumConstants();
            var eValue=envValue(env);
            if(eValue!=null){
                for (var e : enumList) {
                    if (eValue.equalsIgnoreCase(e.toString()))
                        return e;
                }
            }
        }
        return defaultValue;
    }

    public <T> T asEnum(String env, Class<T> enumClass) {
        return this.asEnum(env, enumClass, null);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass) {
        return asEnums(env, enumClass, null);
    }

    public <T> List<T> asEnums(String env, Class<?> enumClass, List<T> defaultValue) {
        if (enumClass != null && enumClass.isEnum()){
            var enumList = enumClass.getEnumConstants();
            var eValue=envValue(env);
            if(eValue!=null){
                List<T> __return = new ArrayList<>();
                var values = eValue.split(",");
                for (var s : values) {
                    for (var e : enumList) {
                        if (e.toString().equalsIgnoreCase(s))
                            __return.add((T) e);
                    }
                }
                return __return;
            }
        }
        return defaultValue==null?new ArrayList<>():defaultValue;
    }

    public List<String> asListOfString(String env) {
        return this.asListOfString(env,null);
    }

    public List<String> asListOfString(String env, List<String> defaultValue) {
        var eValue = envValue(env);
        if(eValue!=null){
            var values = List.of(eValue.split(","));
            List<String> out = new ArrayList<>();
            for (String s : values) {
                if (s != null && !s.trim().isEmpty())
                    out.add(s);
            }
            return out;
        }
        return defaultValue==null?new ArrayList<>():defaultValue;
    }

    public List<Long> asListOfLong(String env) {
        return this.asListOfLong(env,null);
    }

    public List<Long> asListOfLong(String env, List<Long> defaultValue) {
        var eValue = envValue(env);
        List<Long> out = new ArrayList<>();
        if(eValue!=null){
            var values = List.of(eValue.split(","));
            for (String s : values) {
                if (s != null && !s.trim().isEmpty()) {
                    var value = PrimitiveUtil.toLong(s);
                    if (String.valueOf(value).equals(s))
                        out.add(value);
                }
            }
            return out;
        }
        return defaultValue==null?new ArrayList<>():defaultValue;
    }

    public List<Integer> asListOfInt(String env) {
        return this.asListOfInt(env,null);
    }

    public List<Integer> asListOfInt(String env, List<Integer> defaultValue) {
        var eValue = envValue(env);
        if(eValue!=null){
            List<Integer> out = new ArrayList<>();
            var values = List.of(eValue.split(","));
            for (String s : values) {
                if (s != null && !s.trim().isEmpty()) {
                    var value = PrimitiveUtil.toInt(s);
                    if (String.valueOf(value).equals(s))
                        out.add(value);
                }
            }
            return out;
        }
        return defaultValue==null?new ArrayList<>():defaultValue;
    }

}

