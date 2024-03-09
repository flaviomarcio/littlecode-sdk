package com.littlecode.tests;

import com.littlecode.files.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class IOUtilTest {
    private static final File PATH_TEMP_DIR = Path.of(System.getProperty("java.io.tmpdir")).toFile();

    @Test
    public void UT_CHECK_TEMP_FILE() {
        Assertions.assertEquals(IOUtil.tempDir().toString(), PATH_TEMP_DIR.toString());
        Assertions.assertNotNull(IOUtil.createFileTemp());
        Assertions.assertNotNull(IOUtil.createFileTemp("test_"));
        Assertions.assertNotNull(IOUtil.createFileTemp("test_", "_file"));
        Assertions.assertNotNull(IOUtil.createFileTemp("test_", "_file", PATH_TEMP_DIR));
        Assertions.assertNotNull(IOUtil.createFileTemp(PATH_TEMP_DIR));
    }

    @Test
    public void UT_CHECK_READ_WRITE() {
        var fileTemp = IOUtil.createFileTemp();
        var ioUtil = IOUtil.target(fileTemp);
        var lines = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        Assertions.assertDoesNotThrow(() -> ioUtil.delete());
        Assertions.assertFalse(ioUtil.exists());
        Assertions.assertDoesNotThrow(() -> ioUtil.writeAll("test"));
        Assertions.assertTrue(ioUtil.exists());
        Assertions.assertEquals(ioUtil.readAll(), "test");
        Assertions.assertDoesNotThrow(() -> ioUtil.delete());
        Assertions.assertFalse(ioUtil.exists());
        Assertions.assertDoesNotThrow(() -> ioUtil.writeLines(lines));
        Assertions.assertTrue(ioUtil.exists());
        Assertions.assertEquals(ioUtil.readLines().size(), lines.size());
    }


    @Test
    public void UT_CHECK_FILE_FUNCTION() {

        Assertions.assertTrue(IOUtil.target(IOUtil.tempDir()).isDirectory());
        Assertions.assertTrue(IOUtil.target(IOUtil.tempDir()).toPath().toFile().isDirectory());
        Assertions.assertTrue(IOUtil.target(IOUtil.createFileTemp()).isFile());

        var file = IOUtil.newFile(PATH_TEMP_DIR, "file.tmp");
        Assertions.assertNotNull(file);
        if (!file.toString().isEmpty()) {
            var io = IOUtil.target(file);
            Assertions.assertEquals(io.baseName(), "file.tmp");
            Assertions.assertEquals(io.basePath().toString(), PATH_TEMP_DIR.toString());
            Assertions.assertEquals(io.extension(), "tmp");
            file = IOUtil.newFile(PATH_TEMP_DIR, "file");
            Assertions.assertNotNull(file);
        }


        if (!file.toString().isEmpty()) {
            var io = IOUtil.target(file);
            Assertions.assertEquals(io.extension(), "");
            Assertions.assertEquals(io.baseName(), "file");
            Assertions.assertEquals(io.basePath().toString(), PATH_TEMP_DIR.toString());
        }


        file = IOUtil.createFileTemp();
        if (!file.toString().isEmpty()) {

            var io = IOUtil.target(file);
            Assertions.assertNotNull(io.baseName());
            Assertions.assertNotNull(io.basePath());

            Assertions.assertTrue(io.exists());
            Assertions.assertDoesNotThrow(() -> io.delete());
            Assertions.assertFalse(io.exists());
            Assertions.assertDoesNotThrow(() -> io.delete());

            Assertions.assertNotNull(io.split());
            Assertions.assertFalse(List.of(io.split()).isEmpty());

            Assertions.assertTrue(io.createFile());
            Assertions.assertTrue(io.exists());
            Assertions.assertDoesNotThrow(() -> io.delete());
            Assertions.assertFalse(io.exists());
        }

        var tmpPath = Path.of(PATH_TEMP_DIR.toString(), UUID.randomUUID().toString());
        if (!tmpPath.toString().isEmpty()) {
            var io = IOUtil.target(tmpPath);
            Assertions.assertTrue(io.createDir().exists());
            Assertions.assertTrue(io.exists());
            Assertions.assertDoesNotThrow(() -> io.delete());
            Assertions.assertFalse(io.exists());
        }

        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp(PATH_TEMP_DIR));
        var fileName = IOUtil.createFileTemp(PATH_TEMP_DIR);
        Assertions.assertNotNull(fileName);


    }

}
