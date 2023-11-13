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

public class SettingUtil<T> {
    private static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.YML;
    private static final List<FileFormat> FILE_FORMAT_ACCEPTED = List.of(FileFormat.YML, FileFormat.JSON, FileFormat.PROPS, FileFormat.XML);
    private FileFormat fileFormat;
    private File settingFile;

    public SettingUtil() {
        this.fileFormat = null;
        this.settingFile = null;
    }

    public static List<FileFormat> getFileFormats() {
        return FILE_FORMAT_ACCEPTED;
    }


    private static String getExtension(FileFormat fileFormat) {
        if (fileFormat == null)
            throw new FrameworkException(String.format("Invalid %s:%s", FileFormat.class, fileFormat));
        var ext = switch (fileFormat) {
            case JSON -> ".json";
            case PROPS -> ".properties";
            case XML -> ".xml";
            case YML -> ".yml";
            default -> "";
        };
        if (ext.isEmpty())
            throw new FrameworkException(String.format("Invalid %s:%s", FileFormat.class, fileFormat));
        return ext;
    }

    private static File parseExtension(File file, FileFormat fileFormat) {
        if (file == null)
            throw new FrameworkException("SettingFile is null");

        if (fileFormat == null)
            throw new FrameworkException(String.format("Invalid %s:%s", FileFormat.class, fileFormat));

        var ext = IOUtil.target(file).extension();
        if (!ext.isEmpty())
            return file;
        ext = getExtension(fileFormat);
        if (file.toString().toLowerCase().endsWith(ext))
            return file;
        return new File(file + ext);
    }

    public static <T> Optional<T> load(File file, Class<T> aClass, FileFormat fileFormat) {
        if (file == null)
            throw new FrameworkException("SettingFile is null");
        var bytes = IOUtil.target(file).readAll();
        if (bytes.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(ObjectUtil.createFromString(aClass, bytes, fileFormat));
    }

    public static <T> T save(T object, File file, FileFormat fileFormat) {
        if (file == null)
            throw new FrameworkException("SettingFile is null");
        try {
            var basePath = IOUtil.target(file).basePath();
            if (!IOUtil.target(basePath).createDir().exists())
                throw new FrameworkException(String.format("Base path no exists :%s", basePath));
            file = (file.exists())
                    ? file
                    : parseExtension(file, fileFormat);
            var objectMapper = UtilCoreConfig.newObjectMapper(fileFormat);
            objectMapper.writeValue(file, object);
            return object;
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    public final FileFormat getFileFormat() {
        if (this.fileFormat == null)
            this.fileFormat = FILE_FORMAT_DEFAULT;
        return this.fileFormat;
    }

    public T setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
        //noinspection unchecked
        return (T) this;
    }

    public T fileFormat(FileFormat fileFormat) {
        return this.setFileFormat(fileFormat);
    }

    public File getSettingFile() {
        if (this.settingFile == null)
            return null;
        return parseExtension(this.settingFile, this.fileFormat);
    }

    public T setSettingFile(File file) {
        this.settingFile = file;
        //noinspection unchecked
        return (T) this;
    }

    public T settingFile(String file) {
        return this.setSettingFile(Path.of(file).toFile());
    }

    public T settingFile(File file) {
        return this.setSettingFile(file);
    }

    public T clear() {
        ObjectUtil.clear(this);
        //noinspection unchecked
        return (T) this;
    }

    public T load() {
        return this.load(this.getSettingFile());
    }

    public T load(File file) {
        return this.load(file, this.getFileFormat());
    }

    public T load(File file, FileFormat fileFormat) {
        if (file == null)
            throw new FrameworkException("SettingFile is null");
        file = file.exists()
                ? file
                : parseExtension(file, fileFormat);

        var newValues = load(file, this.getClass(), fileFormat);
        ObjectUtil.update(this, newValues, fileFormat);
        //noinspection unchecked
        return (T) this;
    }

    public T save() {
        return this.save(this.getSettingFile());
    }

    public T save(File file) {
        return save(file, this.getFileFormat());
    }

    public T save(File file, FileFormat fileFormat) {
        //noinspection unchecked
        return save((T) this, file, fileFormat);
    }

}


