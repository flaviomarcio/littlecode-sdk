package com.littlecode.tests;

import com.littlecode.files.PathUtil;
import com.littlecode.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PathUtilTest {
    private static final File PATH_TEMP_DIR = Path.of(System.getProperty("java.io.tmpdir")).toFile();
    private static final File PATH_HOME_DIR = Path.of(System.getProperty("user.home")).toFile();

    @Test
    public void UT_CHECK() {
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toURI()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toPath()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toString()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).listFiles());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).countFiles());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).fileName(UUID.randomUUID()));

        Assertions.assertEquals(SystemUtil.Env.JAVA_TEMP_DIR.toString(), PATH_TEMP_DIR.toString());
        Assertions.assertNotNull(PathUtil.tempFile(PATH_TEMP_DIR.toURI()));
        Assertions.assertNotNull(PathUtil.tempFile(UUID.randomUUID()));
        Assertions.assertNotNull(PathUtil.tempFile());
        Assertions.assertNotNull(PathUtil.fileName(PATH_TEMP_DIR.toPath(), UUID.randomUUID()));
        Assertions.assertNotNull(PathUtil.targetFromTempDir().toFile().toString(), PATH_TEMP_DIR.toString());
        Assertions.assertNotNull(PathUtil.targetFromHomeDir().toFile().toString(), PATH_HOME_DIR.toString());

        var fileName = UUID.randomUUID().toString();
        var fileCheck = Path.of(PATH_TEMP_DIR.toString(), fileName).toString();
        Assertions.assertEquals(fileCheck, PathUtil.targetFromTempDir().part(fileName).toFile().toString());

    }

}
