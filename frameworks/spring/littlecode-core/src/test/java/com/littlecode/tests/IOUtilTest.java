package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.IOUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
    @DisplayName("Deve validar getters")
    public void UT_CHECK_GETTER() {
        Assertions.assertDoesNotThrow(() -> IOUtil.target(null));
        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp"));
        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp").getTarget());
        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp").setTarget("/tmp"));
    }

    @Test
    @DisplayName("Deve validar isEmpty")
    public void UT_CHECK_IsEmpty() {
        Assertions.assertDoesNotThrow(() -> IOUtil.isEmpty(null));
        Assertions.assertDoesNotThrow(() -> IOUtil.isEmpty(""));
        Assertions.assertDoesNotThrow(() -> IOUtil.target(null).isEmpty());
        Assertions.assertFalse(IOUtil.target("test").isEmpty());
        Assertions.assertTrue(IOUtil.isEmpty(null));
        Assertions.assertTrue(IOUtil.isEmpty(""));
        Assertions.assertTrue(IOUtil.target(null).isEmpty());
    }

    @Test
    @DisplayName("Deve validar toString")
    public void UT_CHECK_ToString() {
        Assertions.assertDoesNotThrow(() -> IOUtil.toString("/tmp"));
        Assertions.assertDoesNotThrow(() -> IOUtil.toString(null));
        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp").toString());
        Assertions.assertDoesNotThrow(() -> IOUtil.target(null).toString());

        Assertions.assertEquals(IOUtil.toString("/tmp"),"/tmp");
        Assertions.assertEquals(IOUtil.toString(null),"");

        Assertions.assertEquals(IOUtil.target("/tmp").toString(),"/tmp");
        Assertions.assertEquals(IOUtil.target(null).toString(),"");

    }

    @Test
    @DisplayName("Deve validar basePath e baseName")
    public void UT_CHECK_basePath_baseName() {
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath(null));
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath(" "));
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath("file"));
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath("/"));
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath("///"));
        Assertions.assertDoesNotThrow(() -> IOUtil.basePath("/tmp"));

        Assertions.assertDoesNotThrow(()-> IOUtil.baseName(null));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName(""));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName(" "));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName("file"));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName("/"));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName("///"));
        Assertions.assertDoesNotThrow(()-> IOUtil.baseName("/tmp/test"));
    }


    @Test
    @DisplayName("Deve validar write files")
    public void UT_CHECK_READ_WRITE() {
        var fileTemp = IOUtil.createFileTemp();
        var ioUtil = IOUtil.target(fileTemp);
        var lines = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());

        Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(null, null));
        Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(new File("/tmp/test.txt"), null));
        Assertions.assertDoesNotThrow(() -> IOUtil.writeAll(new File("/tmp/test.txt"), "test"));

        Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(null, null));
        Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(new File("/tmp/test.txt"), null));
        Assertions.assertDoesNotThrow(() -> IOUtil.writeLines(new File("/tmp/test.txt"), List.of("line1","line2")));

        Assertions.assertDoesNotThrow(() -> IOUtil.readAll((String)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.readAll((File)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.readAll((Path)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.readAll("/tmp/test.txt"));
        Assertions.assertDoesNotThrow(() -> IOUtil.readAll(Path.of("/tmp/test.txt")));
        Assertions.assertDoesNotThrow(() -> IOUtil.readAll(new File("/tmp/test.txt")));
        Assertions.assertEquals(IOUtil.readAll((String)null),"");
        Assertions.assertEquals(IOUtil.readAll((Path) null),"");
        Assertions.assertEquals(IOUtil.readAll((File)null),"");

        Assertions.assertDoesNotThrow(() -> IOUtil.readLines((String)null ));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines((File)null ));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines((Path)null ));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines(Mockito.mock(Path.class)));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines(new File("/tmp/test.txt")));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines(Path.of("/tmp/test.txt")));
        Assertions.assertDoesNotThrow(() -> IOUtil.readLines("/tmp/test.txt"));
        Assertions.assertTrue(IOUtil.readLines((String)null ).isEmpty());
        Assertions.assertTrue(IOUtil.readLines((File)null ).isEmpty());
        Assertions.assertTrue(IOUtil.readLines((Path)null ).isEmpty());


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
    @DisplayName("Deve validar file functions")
    public void UT_CHECK_FILE_FUNCTION() {

        Assertions.assertEquals(IOUtil.tempDir().toString(), PATH_TEMP_DIR.toString());
        Assertions.assertNotNull(IOUtil.createFileTemp());
        Assertions.assertNotNull(IOUtil.createFileTemp("test_"));
        Assertions.assertNotNull(IOUtil.createFileTemp("test_", "_file"));
        Assertions.assertNotNull(IOUtil.createFileTemp("test_", "_file", PATH_TEMP_DIR));
        Assertions.assertNotNull(IOUtil.createFileTemp(PATH_TEMP_DIR));

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


        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp((File)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp(new File("/tmp/test.txt")));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp("a","b", new File("/tmp")));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp("a","b", null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp("a",null, null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp(null,null, null));
        Assertions.assertNull(IOUtil.createFileTemp(null,null, null));

        Assertions.assertDoesNotThrow(() -> IOUtil.createFileEmpty((String)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileEmpty(""));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileEmpty((File)null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileEmpty(new File("/tmp/file.txt")));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileEmpty("/tmp/file.txt"));
        Assertions.assertFalse(IOUtil.createFileEmpty(new File("/tmp")));
        Assertions.assertFalse(IOUtil.createFileEmpty("/tmp"));
        Assertions.assertFalse(IOUtil.createFileEmpty(""));
        Assertions.assertFalse(IOUtil.createFileEmpty((String)null));
        Assertions.assertFalse(IOUtil.createFileEmpty((File) null));
        Assertions.assertDoesNotThrow(() -> IOUtil.createFileTemp());

        file = IOUtil.createFileTemp();
        if (!file.toString().isEmpty()) {

            var io = IOUtil.target(file);

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

    @Test
    @DisplayName("Deve validar metodo new file")
    public void UT_CHECK_METHOD_NEW_FILE() {

        Assertions.assertThrows(FrameworkException.class, IOUtil::newFile);
        Assertions.assertThrows(FrameworkException.class, () -> IOUtil.newFile(null));
        Assertions.assertDoesNotThrow(() -> IOUtil.newFile("/tmp",UUID.randomUUID()));

    }

    @Test
    @DisplayName("Deve validar metodo create file")
    public void UT_CHECK_METHOD_CREATE_FILE() {

        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp/"+UUID.randomUUID()));
        var io=IOUtil.target("/tmp/"+UUID.randomUUID());
        Assertions.assertDoesNotThrow(() -> io.readAll());
        io.createFile();
        Assertions.assertDoesNotThrow(() -> io.readAll());
    }

    @Test
    @DisplayName("Deve validar metodo read all lines")
    public void UT_CHECK_METHOD_READ_ALL_LINES() {

        Assertions.assertDoesNotThrow(() -> IOUtil.target("/tmp/"+UUID.randomUUID()));
        {
            var io=IOUtil.target("/tmp/"+UUID.randomUUID());
            Assertions.assertDoesNotThrow(() -> io.createFile());
            Assertions.assertFalse(io.createFile());
        }

        {
            var io=IOUtil.target("/tmp/"+UUID.randomUUID()).createDir();
            Assertions.assertDoesNotThrow(() -> io.createFile());
            Assertions.assertFalse(io.createFile());
        }

    }

    @Test
    @DisplayName("Deve validar metodo delete")
    public void UT_CHECK_METHOD_DELETE() {

        {
            var io = IOUtil.target("/tmp/"+UUID.randomUUID());
            io.createFile();
            Assertions.assertDoesNotThrow(() -> io.createDir().delete());
        }

        {
            var dir="/tmp/"+UUID.randomUUID();
            var io = IOUtil.target(dir);
            Assertions.assertDoesNotThrow(() -> io.delete());
            Assertions.assertDoesNotThrow(() -> io.createDir());
            IOUtil.target(dir+"/"+UUID.randomUUID()).createFile();
            Assertions.assertDoesNotThrow(() -> io.createDir().delete());
        }

    }

}
