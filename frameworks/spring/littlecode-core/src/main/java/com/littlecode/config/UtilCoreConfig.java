package com.littlecode.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlecode.files.FileFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class UtilCoreConfig {
    public static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.JSON;
    private static ApplicationContext STATIC_CONTEXT;
    private static Environment STATIC_ENVIRONMENT;

    public UtilCoreConfig(ApplicationContext applicationContext, Environment environment) {
        setApplicationContext(applicationContext);
        setEnvironment(environment);
    }

    public static ApplicationContext getApplicationContext() {
        return STATIC_CONTEXT;
    }

    public static void setApplicationContext(ApplicationContext context) {
        STATIC_CONTEXT = context;
    }

    public static Environment getEnvironment() {
        return STATIC_ENVIRONMENT;
    }

    public static void setEnvironment(Environment environment) {
        STATIC_ENVIRONMENT = environment;
    }

    public static JsonFactory getFactoryFromFileFormat(FileFormat fileFormat) {
        if (fileFormat != null) {
            switch (fileFormat) {
                case YML:
                    return new YAMLFactory();
                case PROPS:
                    return new JavaPropsFactory();
                case XML:
                    return new XmlFactory();
            }
        }
        return new JsonFactory();
    }

    private static ObjectMapper getObjectMapperFromFileFormat(FileFormat fileFormat) {
        if (fileFormat == FileFormat.XML) {
            var mapper = new XmlMapper();
            mapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            return mapper;
        }
        return new ObjectMapper(getFactoryFromFileFormat(fileFormat));
    }

    public static ObjectMapper newObjectMapper(FileFormat fileFormat, Map<SerializationFeature, Boolean> serialization, Map<DeserializationFeature, Boolean> deserialization) {
        var mapper = getObjectMapperFromFileFormat(fileFormat);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(ObjectConverter.modules());

        serialization.forEach(mapper::configure);
        deserialization.forEach(mapper::configure);

        return mapper;
    }

    public static ObjectMapper newObjectMapper(FileFormat fileFormat) {
        return newObjectMapper(fileFormat,
                Map.of(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true),
                Map.of(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
    }

    public static ObjectMapper newObjectMapper() {
        return newObjectMapper(FILE_FORMAT_DEFAULT);
    }

    public static ModelMapper newModelMapper() {
        var mapper = new ModelMapper();
        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        ObjectConverter
                .converters()
                .forEach(mapper::addConverter);
        return mapper;
    }

    @Bean
    public ApplicationContext applicationContext() {
        return STATIC_CONTEXT;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ObjectConverter {

        private static final String FORMAT_DATE = "yyyy-MM-dd";
        private static final String FORMAT_TIME = "HH:mm:ss";
        private static final String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        public static final Converter<String, LocalDate> toLocalDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                return (source == null)
                        ? null
                        : LocalDate.parse(source, DateTimeFormatter.ofPattern(FORMAT_DATE));
            }
        };
        public static final Converter<LocalDate, String> toLocalDateString = new AbstractConverter<LocalDate, String>() {
            @Override
            protected String convert(LocalDate source) {
                return (source == null)
                        ? null
                        : source.format(DateTimeFormatter.ofPattern(FORMAT_DATE));
            }
        };
        public static final Converter<String, LocalTime> toLocalTime = new AbstractConverter<String, LocalTime>() {
            @Override
            protected LocalTime convert(String source) {
                return (source == null)
                        ? null
                        : LocalTime.parse(source, DateTimeFormatter.ofPattern(FORMAT_TIME));
            }
        };
        public static final Converter<LocalTime, String> toLocalTimeString = new AbstractConverter<LocalTime, String>() {
            @Override
            protected String convert(LocalTime source) {
                return (source == null)
                        ? null
                        : source.format(DateTimeFormatter.ofPattern(FORMAT_TIME));
            }
        };
        public static final Converter<String, LocalDateTime> toLocalDateTime = new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String source) {
                return (source == null)
                        ? null
                        : LocalDateTime.parse(source, DateTimeFormatter.ofPattern(FORMAT_DATETIME));
            }
        };
        public static final Converter<LocalDateTime, String> toLocalDateTimeString = new AbstractConverter<LocalDateTime, String>() {
            @Override
            protected String convert(LocalDateTime source) {
                return (source == null)
                        ? null
                        : source.format(DateTimeFormatter.ofPattern(FORMAT_DATETIME));
            }
        };

        public static SimpleModule modules() {
            SimpleModule modules = new SimpleModule();
            modules.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_DATE)));
                }
            });
            modules.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_TIME)));
                }
            });
            modules.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern(FORMAT_DATETIME)));
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

}
