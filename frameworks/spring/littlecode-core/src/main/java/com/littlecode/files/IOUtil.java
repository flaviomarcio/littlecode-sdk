package com.littlecode.files;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.util.SystemUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@Slf4j
public class IOUtil {
    private static final int CR = 13;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final String DIR_SEPARATOR=String.valueOf(File.separatorChar);
    private static final String DIR_SEPARATOR_DOUBLE = DIR_SEPARATOR + DIR_SEPARATOR;
    private static final char DIR_SEPARATOR_UNIX = '/';
    private static final char DIR_SEPARATOR_WINDOWS = '\\';
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static final int EOF = -1;
    public static final int LF = 10;
    private static String STRING_CLASS_NAME = String.class.getName();
    private static String PATH_CLASS_NAME = Path.class.getName();
    private static String PREFIX_TEMP_FILE = "tmp";
    private Object target;

    public static IOUtil target(Object target) {
        if (target == null)
            throw new FrameworkException("target is null");
        return IOUtil.builder().target(target).build();
    }
    public static String toString(Object target) {
        if (target != null) {
            if (target instanceof String)
                return target.toString();
            else if (target instanceof Path)
                return target.toString();
            else if (target instanceof URI)
                return target.toString();
            else if (target instanceof File)
                return ((File) target).getAbsolutePath();
        }
        return "";
    }
    public static Path toPath(Object target) {
        var str = IOUtil.toString(target);
        return str.isEmpty() ? null : Path.of(str);
    }
    public static File toFile(Object target) {
        var str = IOUtil.toString(target);
        return str.isEmpty() ? null : new File(str);
    }

    public static boolean isEmpty(Object target) {
        return IOUtil.toString(target).isEmpty();
    }
    public static String[] split(Object target) {
        var str = IOUtil.toString(target);
        return str.isEmpty() ? new String[0] : str.split(DIR_SEPARATOR);
    }
    public static boolean isFile(Object target) {
        return new File(IOUtil.toString(target)).isFile();
    }
    public static boolean isDirectory(Object target) {
        return new File(IOUtil.toString(target)).isDirectory();
    }
    public static boolean exists(Object target) {
        final var file = IOUtil.toFile(target);
        return file != null && file.exists();
    }
    public static boolean delete(Object target) {
        final var file = IOUtil.toFile(target);
        if (file != null) {
            if (file.exists() && file.isFile())
                return file.delete();
            else {
                try {
                    boolean __return = true;
                    for (var itemFile : Objects.requireNonNull(file.listFiles())) {
                        if (!IOUtil.delete(itemFile))
                            __return = false;
                    }
                    return __return && file.delete();
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }
    public static File newFile(Object... args) {
        if (args != null) {
            var fileParty = new StringBuilder();
            for (var o : args) {
                fileParty
                        .append(DIR_SEPARATOR)
                        .append(IOUtil.toString(o));
            }
            String fileName = fileParty.toString();
            while (fileName.contains(DIR_SEPARATOR_DOUBLE))
                fileName = fileName.replace(DIR_SEPARATOR_DOUBLE, DIR_SEPARATOR);
            return new File(fileName);
        }
        return null;
    }
    public static boolean createDir(Object target) {
        var file = IOUtil.toFile(target);
        return (file != null) && (
                (file.exists() && file.isDirectory()) || file.mkdirs()
        );
    }
    public static String extension(Object target) {
        if (target != null) {
            var extSplit = IOUtil.baseName(target).split("\\.");
            return
                    (extSplit.length <= 1)
                            ? ""
                            : extSplit[extSplit.length - 1].trim();
        }
        return "";
    }
    public static String baseName(Object target) {
        var list = IOUtil.split(target);
        var baseName = (list.length == 0) ? "" : list[list.length - 1];
        return baseName;
    }
    public static File basePath(Object target) {
        var list = IOUtil.split(target);
        if (list.length == 0)
            return null;
        var out = new StringBuilder();
        for (int i = 0; i <= list.length - 2; i++) {
            var str = list[i];
            if (isEmpty(str))
                continue;
            out
                    .append(DIR_SEPARATOR)
                    .append(str);
        }
        return new File(out.toString().trim());
    }
    public static String readAll(Object target) {
        var file = IOUtil.toFile(target);
        if (file != null) {
            try {
                var inputStream = new FileInputStream(file);
                var bytes = new String(inputStream.readAllBytes());
                inputStream.close();
                return bytes;
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    public static List<String> readLines(Object target) {
        var file = IOUtil.toPath(target);
        if (file != null) {
            try {
                return Files.readAllLines(file);
            } catch (Exception ignored) {
            }
        }
        return new ArrayList<>();
    }

    public static boolean writeAll(File file, String body) {
        if (body != null && file != null && !file.isDirectory()) {
            if (file.exists())
                file.delete();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(body);
                writer.flush();
                writer.close();
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static boolean writeLines(File file, List<String> lines) {
        StringBuilder str = null;
        if (lines != null) {
            str = new StringBuilder();
            for (var v : lines)
                if (v != null)
                    str.append(v).append("\n");
        }
        return str != null && IOUtil.writeAll(file, str.toString());
    }
    public static File tempDir() {
        return SystemUtil.Env.JAVA_TEMP_DIR.toFile();
    }
    public static File createFileTemp() {
        return createFileTemp(PREFIX_TEMP_FILE, "", null);
    }
    public static File createFileTemp(String prefix) {
        return createFileTemp(prefix, null, null);
    }
    public static File createFileTemp(String prefix, String suffix) {
        return createFileTemp(prefix, suffix, null);
    }
    public static File createFileTemp(File directory) {
        return createFileTemp(null, null, directory);
    }
    public static File createFileTemp(String prefix, String suffix, File directory) {
        try {
            return File.createTempFile(
                    prefix == null ? PREFIX_TEMP_FILE : prefix.trim(),
                    suffix == null ? "" : suffix.trim(),
                    directory == null ? tempDir() : directory);
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    public static boolean createFile(Object target) {
        var file = IOUtil.toFile(target);
        if (file != null) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
                return file.exists();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return IOUtil.toString(this.getTarget());
    }
    public Path toPath() {
        return IOUtil.toPath(this.getTarget());
    }
    public File toFile() {
        return IOUtil.toFile(this.getTarget());
    }
    public boolean isEmpty() {
        return IOUtil.isEmpty(this.getTarget());
    }
    public String[] split() {
        return IOUtil.split(this.getTarget());
    }
    public boolean isFile() {
        return IOUtil.isFile(this.getTarget());
    }
    public boolean isDirectory() {
        return IOUtil.isDirectory(this.getTarget());
    }
    public boolean exists() {
        return IOUtil.exists(this.getTarget());
    }

    public boolean delete() {
        return IOUtil.delete(this.getTarget());
    }
    public boolean createFile() {
        return IOUtil.createFile(this.getTarget());
    }

    public boolean createDir() {
        return IOUtil.createDir(this.getTarget());
    }
    public String extension() {
        return IOUtil.extension(this.getTarget());
    }
    public String baseName() {
        return IOUtil.baseName(this.getTarget());
    }
    public File basePath() {
        return IOUtil.basePath(this.getTarget());
    }
    public String readAll() {
        return IOUtil.readAll(this.toFile());
    }
    public List<String> readLines() {
        return IOUtil.readLines(this.toFile());
    }

    public boolean writeAll(String body) {
        return IOUtil.writeAll(this.toFile(), body);
    }

    public boolean writeLines(List<String> lines) {
        return IOUtil.writeLines(this.toFile(), lines);
    }
}
