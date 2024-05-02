package com.littlecode.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilCoreConfigConverters {

    public static final Converter<String, LocalDate> toLocalDate = new AbstractConverter<String, LocalDate>() {
        @Override
        protected LocalDate convert(String source) {
            return PrimitiveUtil.toDate(source);
        }
    };
    public static final Converter<LocalDate, String> toLocalDateString = new AbstractConverter<LocalDate, String>() {
        @Override
        protected String convert(LocalDate source) {
            return PrimitiveUtil.toString(source);
        }
    };
    public static final Converter<String, LocalTime> toLocalTime = new AbstractConverter<String, LocalTime>() {
        @Override
        protected LocalTime convert(String source) {
            return PrimitiveUtil.toTime(source);
        }
    };
    public static final Converter<LocalTime, String> toLocalTimeString = new AbstractConverter<LocalTime, String>() {
        @Override
        protected String convert(LocalTime source) {
            return PrimitiveUtil.toString(source);
        }
    };
    public static final Converter<String, LocalDateTime> toLocalDateTime = new AbstractConverter<String, LocalDateTime>() {
        @Override
        protected LocalDateTime convert(String source) {
            return PrimitiveUtil.toDateTime(source);
        }
    };
    public static final Converter<LocalDateTime, String> toLocalDateTimeString = new AbstractConverter<LocalDateTime, String>() {
        @Override
        protected String convert(LocalDateTime source) {
            return PrimitiveUtil.toString(source);
        }
    };
    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";

    public static SimpleModule modules() {
        SimpleModule modules = new SimpleModule();
        modules.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(PrimitiveUtil.toString(value));
            }
        });
        modules.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(PrimitiveUtil.toString(value));
            }
        });
        modules.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(PrimitiveUtil.toString(value));
            }
        });
        return modules;
    }

    public static List<Converter<?, ?>> converters() {
        return List.of(
                toLocalDate, toLocalDateString,
                toLocalTime, toLocalTimeString,
                toLocalDateTime, toLocalDateTimeString
        );
    }

}
