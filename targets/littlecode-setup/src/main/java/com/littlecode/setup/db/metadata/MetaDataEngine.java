package com.littlecode.setup.db.metadata;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.SetupMetaObject;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.privates.MetaDataEngineBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataEngine extends MetaDataEngineBase<MetaDataEngine> {
    private static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static final String CLASS_EXTENSION = ".class";
    private Connection connection;
    private String[] packagesBase;
    private String[] packagesIgnored;
    private SetupSetting setting;

    private static List<Class<?>> getClassesInPackage(String packageName, @SuppressWarnings("SameParameterValue") Class<? extends Annotation> annotationClass) {

        if (annotationClass == null) {
            return new ArrayList<>();
        }

        List<Class<?>> classes = new ArrayList<>();
        try {
            var reflections = PrimitiveUtil.isEmpty(packageName)
                    ? new Reflections()
                    : new Reflections(packageName);
            var scanClasses = reflections.getTypesAnnotatedWith(annotationClass);
            try {
                scanClasses
                        .forEach(aClass -> {
                            if (aClass.isAnnotationPresent(SetupMetaObject.class)) {
                                var an = aClass.getAnnotation(SetupMetaObject.class);
                                if (an.metaDataIgnore())
                                    return;
                            }
                            classes.add(aClass);
                        });
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        classes.sort(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> aClass1, Class<?> aClass2) {
                return aClass1.getName().toLowerCase().compareTo(aClass2.getName().toLowerCase());
            }
        });
        return classes;

    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    public MetaDataEngine load() {
        this.clear();
        List<Class<?>> tableClasses = new ArrayList<>();
        packagesToWork()
                .forEach(
                        packageName ->
                                tableClasses.addAll(getClassesInPackage(packageName, Table.class))
                );

        List<MetaDataClasses.MetaTable> tables = new ArrayList<>();
        tableClasses.forEach(aClass -> {
            var table = new MetaDataClasses.MetaTable(aClass);
            if (PrimitiveUtil.isEmpty(table.getSchemaName()) && this.getSetting() != null)
                table.setSchemaName(this.getSetting().getConfig().getDefaultSchema());
            tables.add(table);
        });

        return this
                .clear()
                .sources(tables);
    }

    private List<String> packagesToWork() {
        List<String> __return = new ArrayList<>();

        List<String> __returnNotWork = new ArrayList<>();
        if (packagesIgnored != null) {
            for (var name : packagesIgnored)
                __returnNotWork.add(name.toLowerCase());
        }

        if (packagesBase != null) {
            for (var name : packagesBase) {
                if (!__returnNotWork.contains(name.toLowerCase()))
                    __return.add(name);
            }
        }

        return __return;
    }
}
