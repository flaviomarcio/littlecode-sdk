package com.littlecode.tests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import com.littlecode.files.IOUtil;
import com.littlecode.setting.SettingLoader;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SettingLoaderTest {

    private SettingTest makeSetting(FileFormat fileFormat) {
        return SettingTest
                .builder()
                .subA(SettingSubTest
                        .builder()
                        .propertyA(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyB(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyC(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyD(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .build()
                )
                .subB(SettingSubTest
                        .builder()
                        .propertyA(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyB(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyC(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyD(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .build()
                )
                .subC(SettingSubTest
                        .builder()
                        .propertyA(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyB(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyC(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyD(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .build()
                )
                .subD(SettingSubTest
                        .builder()
                        .propertyA(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyB(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyC(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .propertyD(SettingPropertyTest.builder().uuid(UUID.randomUUID()).build())
                        .build()
                )
                .build()
                .settingFile(IOUtil.newFile(IOUtil.tempDir(), UUID.randomUUID(), UUID.randomUUID().toString()).getAbsolutePath())
                .settingFile(IOUtil.newFile(IOUtil.tempDir(), UUID.randomUUID(), UUID.randomUUID().toString()))
                .fileFormat(fileFormat)
                .clear();

    }

    @Test
    public void UT_VALID_Getter(){
        SettingTest setting = makeSetting(FileFormat.JSON);
        Assertions.assertDoesNotThrow(()->setting.getSettingFile());
        setting.setSettingFile(null);
        Assertions.assertDoesNotThrow(()->setting.getSettingFile());
    }

    @Test
    @DisplayName("Deve validar fileFormat")
    public void UT_CHECKER_fileFormat() {
        var settingTest=new SettingTest();
        Assertions.assertDoesNotThrow(() -> settingTest.setFileFormat(null));
        Assertions.assertDoesNotThrow(settingTest::getFileFormat);
        Assertions.assertNotNull(settingTest.getFileFormat());
        Assertions.assertEquals(settingTest.getFileFormat(),SettingTest.FILE_FORMAT_DEFAULT);

        Assertions.assertDoesNotThrow(() -> settingTest.setFileFormat(FileFormat.JSON));
        Assertions.assertDoesNotThrow(settingTest::getFileFormat);
        Assertions.assertNotNull(settingTest.getFileFormat());
        Assertions.assertEquals(settingTest.getFileFormat(),FileFormat.JSON);

    }

    @Test
    @DisplayName("Deve validar get extensions")
    public void UT_CHECKER_getExtension() {
        for (FileFormat e : List.of(FileFormat.values())) {
            Assertions.assertDoesNotThrow(() -> SettingTest.getExtension(e));
            Assertions.assertEquals(SettingTest.getExtension(e), "." + e.name().toLowerCase());
        }

        Assertions.assertDoesNotThrow(()->SettingTest.getExtension(null));
        Assertions.assertEquals(SettingTest.getExtension(null),"");

    }

    @Test
    @DisplayName("Deve validar parser extensions")
    public void UT_CHECKER_parseExtension() {
        var file=new File("/tmp/file");
        var fileJson=new File("/tmp/file.json");

        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(file,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON));
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,null));
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(null, null));

        Assertions.assertEquals(SettingTest.parseExtension(file,FileFormat.JSON).getAbsolutePath(),fileJson.getAbsolutePath());
        Assertions.assertEquals(SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath(), fileJson.getAbsolutePath());
        Assertions.assertNull(SettingTest.parseExtension(fileJson,null));
        Assertions.assertNull(SettingTest.parseExtension(null, FileFormat.JSON));
        Assertions.assertNull(SettingTest.parseExtension(null, null));

    }

    @Test
    @DisplayName("Deve validar load")
    public void UT_CHECKER_LOAD() {

        var objSrc=Map.of("a","test");

        Assertions.assertDoesNotThrow(() -> SettingLoader.staticObjectSave(new File("/tmp/file.json"),objSrc, FileFormat.JSON));
        Assertions.assertThrows(FrameworkException.class, () -> SettingLoader.staticObjectSave(new File("/tmp/file.json"),new Object(), FileFormat.JSON));
        Assertions.assertThrows(NullPointerException.class, () -> SettingLoader.staticObjectSave(new File("/tmp/file.json"),new Object(), null));
        Assertions.assertThrows(NullPointerException.class, () -> SettingLoader.staticObjectSave(new File("/tmp/file.json"),null, null));
        Assertions.assertThrows(NullPointerException.class, () -> SettingLoader.staticObjectSave(null,null, null));

        for (FileFormat fileFormat : SettingLoader.FILE_FORMAT_ACCEPTED) {
            Map<FileFormat, File> settingFiles = new HashMap<>();
            SettingTest setting = makeSetting(fileFormat);
            var settingFile = setting
                    .settingFile(IOUtil.createFileTemp())
                    .getSettingFile();
            settingFiles.put(fileFormat, settingFile);

            Assertions.assertNotNull(settingFile);
            Assertions.assertFalse(settingFile.exists());
            Assertions.assertDoesNotThrow(() -> setting.load());
            Assertions.assertDoesNotThrow(() -> setting.load(setting.getSettingFile()));
            Assertions.assertDoesNotThrow(() -> setting.load(null, fileFormat));
        }

    }

    @Test
    @DisplayName("Deve validar save")
    public void UT_CHECKER_SAVE() {

        Map<FileFormat, File> settingFiles = new HashMap<>();

        for (FileFormat fileFormat : SettingLoader.FILE_FORMAT_ACCEPTED) {
            SettingTest setting = makeSetting(fileFormat);
            var settingFile = setting
                    .settingFile(IOUtil.createFileTemp())
                    .getSettingFile();
            settingFiles.put(fileFormat, settingFile);

            Assertions.assertNotNull(settingFile);
            Assertions.assertFalse(settingFile.exists());
            Assertions.assertDoesNotThrow(() -> setting.save(null));
            Assertions.assertDoesNotThrow(() -> setting.save());
            Assertions.assertDoesNotThrow(() -> setting.save(setting.getSettingFile()));
            Assertions.assertTrue(IOUtil.target(setting.getSettingFile()).exists());
        }

        settingFiles
                .forEach(
                        (fileFormat, settingFile) ->
                        {
                            var setting = makeSetting(fileFormat);
                            setting
                                    .fileFormat(fileFormat)
                                    .setSettingFile(settingFile);
                            Assertions.assertDoesNotThrow(() -> setting.load());
                            Assertions.assertTrue(IOUtil.exists(setting.getSettingFile()));
                        }
                );
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SettingTest extends SettingLoader<SettingTest> {
        private SettingSubTest subA;
        private SettingSubTest subB;
        private SettingSubTest subC;
        private SettingSubTest subD;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SettingSubTest {
        @JsonProperty("propA")
        private SettingPropertyTest propertyA;
        @JsonProperty("propB")
        private SettingPropertyTest propertyB;
        @JsonProperty("propC")
        private SettingPropertyTest propertyC;
        @JsonProperty("propD")
        private SettingPropertyTest propertyD;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SettingPropertyTest {
        @JsonProperty("u_u_i_d")
        private UUID uuid;
    }
}
