package com.littlecode.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlecode.files.FileFormat;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
public class UtilCoreConfig {
    public static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.JSON;
    private static ApplicationContext STATIC_CONTEXT;
    private static Environment STATIC_ENVIRONMENT;

    public UtilCoreConfig(ApplicationContext applicationContext, Environment environment) {
        if (applicationContext == null)
            throw new NullPointerException("applicationContext is null");
        if (environment == null)
            throw new NullPointerException("environment is null");
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
        mapper.registerModule(UtilCoreConfigConverters.modules());

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
        UtilCoreConfigConverters
                .converters()
                .forEach(mapper::addConverter);
        return mapper;
    }

}
