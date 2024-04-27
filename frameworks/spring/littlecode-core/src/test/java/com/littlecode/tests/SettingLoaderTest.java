package com.littlecode.tests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.littlecode.files.FileFormat;
import com.littlecode.files.IOUtil;
import com.littlecode.setting.SettingLoader;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
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
                .settingFile(IOUtil.newFile(IOUtil.tempDir(), UUID.randomUUID(), UUID.randomUUID().toString()))
                .fileFormat(fileFormat)
                .clear();

    }

    @Test
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
    public void UT_CHECKER_getExtension() {
        for (FileFormat e : List.of(FileFormat.values())) {
            Assertions.assertDoesNotThrow(() -> SettingTest.getExtension(e));
            Assertions.assertEquals(SettingTest.getExtension(e), "." + e.name().toLowerCase());
        }

        Assertions.assertDoesNotThrow(()->SettingTest.getExtension(null));
        Assertions.assertEquals(SettingTest.getExtension(null),"");

    }

    @Test
    public void UT_CHECKER_parseExtension() {
        var file=new File("/tmp/file.json");
        var fileJson=new File("/tmp/file.json");

        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(file,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath());
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,FileFormat.JSON));
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(fileJson,null));
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(null, FileFormat.JSON));
        Assertions.assertDoesNotThrow(()->SettingTest.parseExtension(null, null));

        Assertions.assertEquals(SettingTest.parseExtension(file,FileFormat.JSON).getAbsolutePath(),file.getAbsolutePath());
        Assertions.assertEquals(SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath(),fileJson.getAbsolutePath());
        Assertions.assertEquals(SettingTest.parseExtension(fileJson,FileFormat.JSON).getAbsolutePath(),fileJson.getAbsolutePath());
        Assertions.assertEquals(SettingTest.parseExtension(fileJson,FileFormat.JSON),fileJson);
        Assertions.assertNull(SettingTest.parseExtension(fileJson,null));
        Assertions.assertNull(SettingTest.parseExtension(null, FileFormat.JSON));
        Assertions.assertNull(SettingTest.parseExtension(null, null));

    }

    @Test
    public void UT_CHECKER_SAVE_LOAD() {

        Map<FileFormat, File> settingFiles = new HashMap<>();

        for (FileFormat fileFormat : SettingLoader.FILE_FORMAT_ACCEPTED) {
            SettingTest setting = makeSetting(fileFormat);
            var settingFile = setting
                    .settingFile(IOUtil.createFileTemp())
                    .getSettingFile();
            settingFiles.put(fileFormat, settingFile);

            Assertions.assertNotNull(settingFile);
            Assertions.assertFalse(settingFile.exists());
            Assertions.assertDoesNotThrow(() -> setting.save());
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
