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
        Assertions.assertDoesNotThrow(()->PathUtil.target(PATH_TEMP_DIR.toURI()).getTarget());
        Assertions.assertDoesNotThrow(()->PathUtil.target(PATH_TEMP_DIR.toURI()).setTarget(Path.of("/tmp/file")));
        Assertions.assertDoesNotThrow(()->PathUtil.listFiles((File)null));
        Assertions.assertDoesNotThrow(()->PathUtil.listFiles((Path)null));
        Assertions.assertDoesNotThrow(()->PathUtil.countFiles(new File("/tmp")));
        Assertions.assertDoesNotThrow(()->PathUtil.countFiles((File)null));
        Assertions.assertDoesNotThrow(()->PathUtil.listFiles(Path.of("/tmp")));
        Assertions.assertDoesNotThrow(()->PathUtil.exists((String)null));
        Assertions.assertDoesNotThrow(()->PathUtil.exists((File)null));
        Assertions.assertDoesNotThrow(()->PathUtil.exists((Path)null));
        Assertions.assertDoesNotThrow(()->PathUtil.exists("/tmp"));
        Assertions.assertDoesNotThrow(()->PathUtil.exists(new File("/tmp")));
        Assertions.assertDoesNotThrow(()->PathUtil.exists(Path.of("/tmp")));

        Assertions.assertDoesNotThrow(()->PathUtil.mkDir((String) null));
        Assertions.assertDoesNotThrow(()->PathUtil.mkDir((Path) null));
        Assertions.assertDoesNotThrow(()->PathUtil.mkDir((File) null));

        Assertions.assertDoesNotThrow(()->PathUtil.mkDir("/tmp/teste"));
        Assertions.assertDoesNotThrow(()->PathUtil.mkDir(Path.of("/tmp/teste")));
        Assertions.assertDoesNotThrow(()->PathUtil.mkDir(new File("/tmp/teste")));

        Assertions.assertDoesNotThrow(()->PathUtil.rmDir((String) null));
        Assertions.assertDoesNotThrow(()->PathUtil.rmDir((Path) null));
        Assertions.assertDoesNotThrow(()->PathUtil.rmDir((File) null));

        Assertions.assertDoesNotThrow(()->PathUtil.rmDir("/tmp/teste"));
        Assertions.assertDoesNotThrow(()->PathUtil.rmDir(Path.of("/tmp/teste")));
        Assertions.assertDoesNotThrow(()->PathUtil.rmDir(new File("/tmp/teste")));

        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").part((String)null));
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").part((UUID)null));
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").part("teste"));
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").part(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").clean());
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").clear());

        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").mkDir());
        Assertions.assertDoesNotThrow(()->PathUtil.target("/tmp/test").rmDir());

        Assertions.assertTrue(PathUtil.mkDir("/tmp/teste"));
        Assertions.assertTrue(PathUtil.mkDir(Path.of("/tmp/teste")));
        Assertions.assertTrue(PathUtil.mkDir(new File("/tmp/teste")));

        Assertions.assertFalse(PathUtil.mkDir((String) null));
        Assertions.assertFalse(PathUtil.mkDir((Path) null));
        Assertions.assertFalse(PathUtil.mkDir((File) null));

        Assertions.assertFalse(PathUtil.rmDir((String) null));
        Assertions.assertFalse(PathUtil.rmDir((Path) null));
        Assertions.assertFalse(PathUtil.rmDir((File) null));

        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toURI()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toPath()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR.toString()).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).toString());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).listFiles());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).countFiles());
        Assertions.assertTrue(PathUtil.target(PATH_TEMP_DIR).exists());
        Assertions.assertNotNull(PathUtil.target(PATH_TEMP_DIR).fileName(UUID.randomUUID()));

        Assertions.assertFalse(PathUtil.exists((String)null));
        Assertions.assertFalse(PathUtil.exists((File)null));
        Assertions.assertFalse(PathUtil.exists((Path)null));
        Assertions.assertTrue(PathUtil.exists("/tmp"));
        Assertions.assertTrue(PathUtil.exists(new File("/tmp")));
        Assertions.assertTrue(PathUtil.exists(Path.of("/tmp")));

        Assertions.assertEquals(PathUtil.countFiles((String)null),0);
        Assertions.assertEquals(PathUtil.countFiles((File)null),0);
        Assertions.assertEquals(PathUtil.countFiles((Path)null),0);

        Assertions.assertEquals(PathUtil.countFiles((File)null),0);
        Assertions.assertNotNull(PathUtil.listFiles((Path)null));
        Assertions.assertNotNull(PathUtil.listFiles((File)null));
        Assertions.assertNotNull(PathUtil.listFiles((Path)null));
        Assertions.assertEquals(SystemUtil.Env.JAVA_TEMP_DIR.toString(), PATH_TEMP_DIR.toString());
        Assertions.assertNotNull(PathUtil.tempFile(UUID.randomUUID().toString()));
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
