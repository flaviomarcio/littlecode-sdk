package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public void UT_CHECK_TO_STRING() {
        Map<String, Object> map = Map.of(
                "/tmp/file1", Path.of("/tmp/file1"),
                "/tmp/file2", URI.create("/tmp/file2"),
                "/tmp/file3", new File("/tmp/file3")
        );
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            Assertions.assertDoesNotThrow(() -> IOUtil.toString(v));
            Assertions.assertNotNull(IOUtil.toString(v));
            Assertions.assertEquals(IOUtil.toString(v), k);

            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.toString());
            Assertions.assertNotNull(ioUtil.toString());
            Assertions.assertEquals(ioUtil.toString(), k);
        }
        Assertions.assertEquals(IOUtil.toString(null), "");
    }

    @Test
    public void UT_CHECK_IS_EMPTY() {
        Map<String, Object> map = Map.of(
                "/tmp/file1", Path.of("/tmp/file1"),
                "/tmp/file2", URI.create("/tmp/file2"),
                "/tmp/file3", new File("/tmp/file3")
        );
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            Assertions.assertDoesNotThrow(() -> IOUtil.isEmpty(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.isEmpty(null));
            Assertions.assertFalse(IOUtil.isEmpty(v));
            Assertions.assertTrue(IOUtil.isEmpty(null));
            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.isEmpty());
            Assertions.assertFalse(ioUtil.isEmpty());
            ioUtil.setTarget(null);
            Assertions.assertDoesNotThrow(() -> ioUtil.isEmpty());
            Assertions.assertTrue(ioUtil.isEmpty());
        }

    }

    @Test
    public void UT_CHECK_TO_FILE() {
        Map<String, Object> map = Map.of(
                "/tmp/file1", Path.of("/tmp/file1"),
                "/tmp/file2", URI.create("/tmp/file2"),
                "/tmp/file3", new File("/tmp/file3")
        );
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            Assertions.assertDoesNotThrow(() -> IOUtil.toFile(v));
            Assertions.assertNotNull(IOUtil.toFile(v));
            Assertions.assertEquals(Objects.requireNonNull(IOUtil.toFile(v)).getAbsolutePath(), k);

            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.toFile());
            Assertions.assertNotNull(ioUtil.toFile());
            Assertions.assertEquals(ioUtil.toFile().getAbsolutePath(), k);
        }
        Assertions.assertThrows(FrameworkException.class, () -> IOUtil.target(null));
        Assertions.assertNull(IOUtil.toFile(null));
    }

    @Test
    public void UT_CHECK_TO_PATH() {

        Map<String, Object> map = Map.of(
                "/tmp/file1", Path.of("/tmp/file1"),
                "/tmp/file2", URI.create("/tmp/file2"),
                "/tmp/file3", new File("/tmp/file3")
        );
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            Assertions.assertDoesNotThrow(() -> IOUtil.toPath(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.toPath(null));
            Assertions.assertNotNull(IOUtil.toPath(v));
            Assertions.assertNull(IOUtil.toPath(null));
            Assertions.assertEquals(Objects.requireNonNull(IOUtil.toPath(v)).toString(), k);

            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.toPath());
            Assertions.assertNotNull(ioUtil.toPath());
            Assertions.assertEquals(ioUtil.toPath().toString(), k);

            ioUtil.setTarget(null);
            Assertions.assertDoesNotThrow(() -> ioUtil.toPath());
            Assertions.assertNull(ioUtil.toPath());
        }
        Assertions.assertNull(IOUtil.toPath(null));

    }


    @Test
    public void UT_CHECK_TO_SPLIT() {

        var list = List.of(
                Path.of("/tmp/file1/1"),
                URI.create("/tmp/file2/2"),
                new File("/tmp/file3/3")
        );
        for (var v : list) {
            Assertions.assertDoesNotThrow(() -> IOUtil.split(v));
            Assertions.assertEquals(IOUtil.split(v).length, 4);

            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.toPath());
            Assertions.assertEquals(ioUtil.split().length, 4);
        }
        Assertions.assertEquals(IOUtil.split(null).length, 0);

    }

    @Test
    public void UT_CHECK_CREATE_DIR() {
        var list = List.of(
                Path.of("/tmp/file1/1"),
                URI.create("/tmp/file2/2"),
                new File("/tmp/file3/3")
        );
        for (var v : list) {
            Assertions.assertDoesNotThrow(() -> IOUtil.exists(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.delete(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.isDirectory(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.isFile(v));
            Assertions.assertFalse(IOUtil.exists(v));
            Assertions.assertFalse(IOUtil.isDirectory(v));
            Assertions.assertFalse(IOUtil.delete(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.createDir(null));
            Assertions.assertFalse(IOUtil.createDir(null));
            Assertions.assertDoesNotThrow(() -> IOUtil.createDir(v));
            Assertions.assertTrue(IOUtil.delete(v));
            Assertions.assertFalse(IOUtil.exists(v));
            Assertions.assertTrue(IOUtil.createDir(v));
            Assertions.assertTrue(IOUtil.exists(v));
            Assertions.assertTrue(IOUtil.isDirectory(v));
            Assertions.assertTrue(IOUtil.delete(v));

            var ioUtil = IOUtil.target(v);
            Assertions.assertDoesNotThrow(() -> ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertDoesNotThrow(() -> ioUtil.isDirectory());
            Assertions.assertDoesNotThrow(() -> ioUtil.createDir());
            Assertions.assertTrue(ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertTrue(ioUtil.createDir());
            Assertions.assertTrue(ioUtil.exists());
            Assertions.assertTrue(ioUtil.isDirectory());
            Assertions.assertTrue(ioUtil.delete());

            ioUtil.setTarget(null);
            Assertions.assertDoesNotThrow(() -> ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertDoesNotThrow(() -> ioUtil.isDirectory());
            Assertions.assertDoesNotThrow(() -> ioUtil.createDir());
            Assertions.assertFalse(ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertFalse(ioUtil.createDir());
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertFalse(ioUtil.isDirectory());
            Assertions.assertFalse(ioUtil.delete());
        }
    }

    @Test
    public void UT_CHECK_CREATE_FILE() {
        final String extension = "tmp";
        var list = List.of(
                Path.of("/tmp/file1/" + UUID.randomUUID() + "." + extension),
                URI.create("/tmp/file2/" + UUID.randomUUID() + "." + extension),
                new File("/tmp/file3/" + UUID.randomUUID() + "." + extension)
        );
        for (var v : list) {
            Assertions.assertDoesNotThrow(() -> IOUtil.createFile(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.createFile(null));
            Assertions.assertFalse(() -> IOUtil.createFile(null));
            Assertions.assertTrue(() -> IOUtil.createFile(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.extension(v), extension);
            Assertions.assertEquals(IOUtil.extension(v), extension);
            Assertions.assertDoesNotThrow(() -> IOUtil.extension(null));
            Assertions.assertEquals(IOUtil.extension(null), "");
            Assertions.assertTrue(IOUtil.isFile(v));
            Assertions.assertTrue(IOUtil.exists(v));
            Assertions.assertTrue(IOUtil.delete(v));
            Assertions.assertFalse(IOUtil.exists(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.createFile(v));
            Assertions.assertTrue(IOUtil.exists(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.delete(v));
            Assertions.assertFalse(IOUtil.exists(v));

            var ioUtil = IOUtil.target(v);
            Assertions.assertEquals(ioUtil.extension(), extension);
            Assertions.assertDoesNotThrow(() -> ioUtil.createFile());
            Assertions.assertTrue(ioUtil.createFile());
            Assertions.assertTrue(ioUtil.exists());
            Assertions.assertTrue(ioUtil.isFile());
            Assertions.assertTrue(ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.createFile());
            Assertions.assertTrue(ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());

            ioUtil.setTarget(null);
            Assertions.assertEquals(ioUtil.extension(), "");
            Assertions.assertDoesNotThrow(() -> ioUtil.createFile());
            Assertions.assertFalse(ioUtil.createFile());
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertFalse(ioUtil.isFile());
            Assertions.assertFalse(ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());
        }
    }

    @Test
    public void UT_CHECK_WRITE_FILE() {


        var list = List.of(
                "/tmp/file1",
                "/tmp/file2",
                "/tmp/file3"
        );
        for (var v : list) {
            List<String> lines = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            var file = IOUtil.toFile(v + "/" + UUID.randomUUID());
            Assertions.assertFalse(IOUtil.exists(file));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(null, null));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(file, null));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(file, UUID.randomUUID().toString()));
            Assertions.assertTrue(IOUtil.writeAll(file, UUID.randomUUID().toString()));
            Assertions.assertTrue(IOUtil.exists(v));
            Assertions.assertDoesNotThrow(() -> IOUtil.readAll(file));
            Assertions.assertFalse(() -> IOUtil.readAll(file).isEmpty());
            Assertions.assertNotNull(IOUtil.readAll(file));
            Assertions.assertDoesNotThrow(() -> IOUtil.delete(file));
            Assertions.assertFalse(IOUtil.exists(file));

            Assertions.assertFalse(IOUtil.exists(file));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(null, null));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(file, null));
            Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(file, lines));
            Assertions.assertTrue(IOUtil.writeLines(file, lines));
            Assertions.assertTrue(IOUtil.exists(file));
            Assertions.assertDoesNotThrow(() -> IOUtil.readLines(file));
            Assertions.assertNotNull(IOUtil.readLines(null));
            Assertions.assertFalse(() -> IOUtil.readLines(file).isEmpty());
            Assertions.assertDoesNotThrow(() -> IOUtil.delete(file));
            Assertions.assertFalse(IOUtil.exists(file));

            var ioUtil = IOUtil.target(file);
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.writeAll(UUID.randomUUID().toString()));
            Assertions.assertDoesNotThrow(() -> ioUtil.writeAll(null));
            Assertions.assertFalse(ioUtil.writeAll(null));
            Assertions.assertTrue(ioUtil.writeAll(UUID.randomUUID().toString()));
            Assertions.assertDoesNotThrow(() -> ioUtil.readLines());
            Assertions.assertFalse(() -> ioUtil.readAll().isEmpty());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());

            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.writeLines(null));
            Assertions.assertFalse(ioUtil.writeLines(null));
            Assertions.assertDoesNotThrow(() -> ioUtil.writeLines(lines));
            Assertions.assertTrue(ioUtil.writeLines(lines));
            Assertions.assertTrue(ioUtil.writeLines(lines));
            Assertions.assertDoesNotThrow(() -> ioUtil.readLines());
            Assertions.assertFalse(() -> ioUtil.readLines().isEmpty());
            Assertions.assertNotNull(ioUtil.readLines());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());

            ioUtil.setTarget(null);
            Assertions.assertFalse(ioUtil.exists());
            Assertions.assertDoesNotThrow(() -> ioUtil.writeLines(null));
            Assertions.assertFalse(ioUtil.writeLines(null));
            Assertions.assertDoesNotThrow(() -> ioUtil.writeLines(lines));
            Assertions.assertFalse(ioUtil.writeLines(lines));
            Assertions.assertFalse(ioUtil.writeLines(lines));
            Assertions.assertDoesNotThrow(() -> ioUtil.readLines());
            Assertions.assertTrue(() -> ioUtil.readLines().isEmpty());
            Assertions.assertNotNull(ioUtil.readLines());
            Assertions.assertDoesNotThrow(() -> ioUtil.delete());
            Assertions.assertFalse(ioUtil.exists());
        }
    }


    @Test
    public void UT_CHECK_READ_WRITE() {
        Assertions.assertDoesNotThrow(IOUtil::tempDir);
        Assertions.assertNotNull(IOUtil.tempDir());
        Assertions.assertEquals(IOUtil.tempDir().getAbsolutePath(), System.getProperty("java.io.tmpdir"));

        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp());
        Assertions.assertNotNull(IOUtil.createFileTemp());

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
            Assertions.assertTrue(io.createDir());
            Assertions.assertTrue(io.exists());
            Assertions.assertDoesNotThrow(() -> io.delete());
            Assertions.assertFalse(io.exists());
        }

        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp(PATH_TEMP_DIR));
        var fileName = IOUtil.createFileTemp(PATH_TEMP_DIR);
        Assertions.assertNotNull(fileName);


    }

}
