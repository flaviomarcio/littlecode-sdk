package com.littlecode.setting;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import com.littlecode.files.IOUtil;
import com.littlecode.parsers.ObjectUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class SettingLoader<T>  {
    public static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.YML;
    public static final List<FileFormat> FILE_FORMAT_ACCEPTED = List.of(FileFormat.YML, FileFormat.JSON, FileFormat.PROPS, FileFormat.XML);
    private FileFormat fileFormat;
    private File settingFile;

    public SettingLoader() {
        this.fileFormat = null;
        this.settingFile = null;
    }


    public static String getExtension(FileFormat fileFormat) {
        return (fileFormat == null)
                ?""
                :"."+fileFormat.name().toLowerCase();
    }

    public static File parseExtension(final File file, FileFormat fileFormat) {
        if (file != null && fileFormat!=null){
            var ext = IOUtil.target(file).extension();
            if (!ext.isEmpty())
                return file;
            ext = getExtension(fileFormat);
            var fileName=file.getAbsolutePath().toLowerCase();
            if (fileName.endsWith(ext))
                return file;
            return new File(file + ext);
        }
        return null;
    }

    public static boolean save(Object object, final File file, FileFormat fileFormat) {
        if (file != null){
            var basePath = IOUtil.target(file).basePath();
            var ioUtil=IOUtil.target(basePath);
            if (ioUtil.createDir().exists()){
                var fileSave = file.exists()
                        ? file
                        : parseExtension(file, fileFormat);
                var objectMapper = UtilCoreConfig.newObjectMapper(fileFormat);
                try {
                    objectMapper.writeValue(fileSave, object);
                    return true;
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    public final FileFormat getFileFormat() {
        if (this.fileFormat == null)
            return this.fileFormat = FILE_FORMAT_DEFAULT;
        return this.fileFormat;
    }

    public T setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        return (T)this;
    }

    public T fileFormat(FileFormat fileFormat) {
        this.setFileFormat(fileFormat);
        return (T)this;
    }

    public File getSettingFile() {
        if (this.settingFile == null)
            return null;
        return parseExtension(this.settingFile, this.fileFormat);
    }

    public T setSettingFile(File file) {
        this.settingFile = file;
        return (T)this;
    }

    public T settingFile(String file) {
        this.setSettingFile(Path.of(file).toFile());
        return (T)this;
    }

    public T settingFile(File file) {
        this.setSettingFile(file);
        return (T)this;
    }

    public T clear() {
        ObjectUtil.clear(this);
        return (T)this;
    }

    public boolean load() {
        return this.load(this.getSettingFile());
    }

    public boolean load(File file) {
        return this.load(file, this.getFileFormat());
    }

    public boolean load(File file, FileFormat fileFormat) {
        if (file != null){
            file = file.exists()
                    ? file
                    : parseExtension(file, fileFormat);

            ObjectUtil.update(this, file);
            return true;
        }
        return false;
    }

    public boolean save() {
        return this.save(this.getSettingFile());
    }

    public boolean save(File file) {
        return save(file, this.getFileFormat());
    }

    public boolean save(File file, FileFormat fileFormat) {
        return save(this, file, fileFormat);
    }

}