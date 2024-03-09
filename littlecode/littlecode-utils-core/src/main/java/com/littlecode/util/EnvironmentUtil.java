package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
            return environment.getProperty(env);
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

}

