package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
public class SystemUtil {

    @SneakyThrows
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }

    //ref https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html
    public static class Env {
        public static final Path JAVA_TEMP_DIR = Path.of(System.getProperty("java.io.tmpdir"));
        public static final Path JAVA_LIBRARY_PATH = Path.of(System.getProperty("java.library.path"));
        public static final String OS_NAME = getSystemProperty("os.name");
        public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
        public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
        public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
        public static final String USER_NAME = getSystemProperty("user.name");
        public static final Path USER_HOME = Path.of(getSystemProperty("user.home"));
        public static final Path USER_DIR = Path.of(getSystemProperty("user.dir"));
        public static final Path JAVA_HOME = Path.of(getSystemProperty("java.home"));
        public static final String APP_NAME = getSystemProperty("spring.application.name");

        public static String getSystemProperty(String propertyName) {
            return getSystemProperty(propertyName, null);
        }

        public static String getSystemProperty(String propertyName, String defaultValue) {
            if (propertyName != null) {
                var value = System.getProperty(propertyName);
                if (value != null)
                    return value.trim();
            }
            return defaultValue == null ? "" : defaultValue.trim();
        }

        public static String getProperty(String propertyName, String defaultValue) {
            if (propertyName != null && !propertyName.trim().isEmpty()) {
                var environment = UtilCoreConfig.getEnvironment();
                if (environment.containsProperty(propertyName))
                    return environment.getProperty(propertyName);

                var value = System.getProperty(propertyName);
                if (value != null)
                    return value.trim();
            }
            return defaultValue == null ? "" : defaultValue.trim();
        }

        public static String getProperty(String propertyName) {
            return getProperty(propertyName, null);
        }

        public static String getAppName() {
            return getProperty("spring.application.name");
        }

    }
}