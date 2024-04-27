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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static String PREFIX_TEMP_FILE = "tmp";
    private Object target;

    public static IOUtil target(Object target) {
        return IOUtil.builder().target(target).build();
    }

    public static String toString(Object target) {
        if (target != null) {
            if (target instanceof String)
                return ((String) target).trim();
            else if (target instanceof Path)
                return ((Path) target).toString();
            else
                return target.toString().trim();
        }
        return "";
    }

    public static Path toPath(Object target) {
        return Path.of(toString(target));
    }

    public static File toFile(Object target) {
        return toPath(target).toFile();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String[] split(Object target) {
        return toString(target).split(DIR_SEPARATOR);
    }

    public static boolean isFile(Object target) {
        return toFile(target).isFile();
    }

    public static boolean isDirectory(Object target) {
        return toFile(target).isDirectory();
    }

    public static boolean exists(Object target) {
        return toFile(target).exists();
    }

    public static boolean delete(Object target) {
        var file = toFile(target);
        if (!file.exists())
            return false;
        if (!file.canWrite())
            return false;
        if (file.isFile())
            return file.delete();

        boolean __return = true;
        for (var itemFile : Objects.requireNonNull(file.listFiles())) {
            if (!delete(itemFile))
                __return = false;
        }
        return __return && file.delete();
    }

    public static File newFile(Object... args) {
        if (args == null || args.length == 0)
            throw new FrameworkException("Invalid args");
        var fileParty = new StringBuilder();
        for (var arg : args) {
            var str = toString(arg);
            if (isEmpty(str))
                continue;
            fileParty
                    .append(DIR_SEPARATOR)
                    .append(str);
        }
        String fileName = fileParty.toString();
        while (fileName.contains(DIR_SEPARATOR_DOUBLE))
            fileName = fileName.replace(DIR_SEPARATOR_DOUBLE, DIR_SEPARATOR);
        return Path.of(fileName).toFile();
    }

    public static boolean createFile(Object target) {
        var file = toFile(target);
        if (file.exists()) {
            if (file.isDirectory())
                return false;
            return file.isFile();
        }
        return createFileEmpty(file);
    }

    public static boolean createDir(Object target) {
        var file = toFile(target);
        if (file.exists())
            return file.isDirectory();
        return file.mkdirs();
    }

    public static String extension(Object target) {
        var extSplit = baseName(target).split("\\.");
        if (extSplit.length <= 1)
            return "";
        return extSplit[extSplit.length - 1].trim();
    }

    public static String baseName(Object target) {
        var list = split(target);
        var baseName = (list.length == 0) ? "" : list[list.length - 1];
        return baseName == null
                ? ""
                : baseName.trim();
    }

    public static File basePath(Object target) {
        var list = split(target);
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
        File updateFile = null;
        //noinspection ConstantValue
        if (target.getClass().equals(Path.class))
            updateFile = ((Path) target).toFile();
        else if (target.getClass().equals(File.class))
            updateFile = (File) target;
        else if (target.getClass().equals(String.class)) {
            var file = Path.of(((String) target).trim()).toFile();
            if (file.exists() && file.isFile())
                updateFile = file;
        }
        if (updateFile == null || !updateFile.exists() || !updateFile.isFile())
            return "";

        try (var inputStream = new FileInputStream(updateFile)) {
            StringBuilder str = new StringBuilder();
            int content;
            while ((content = inputStream.read()) != -1)
                str.append((char) content);
            return str.toString().trim();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            return "";
        }
    }

    public static List<String> readLines(File target) {
        if (target == null || !target.exists() || !target.isFile())
            return new ArrayList<>();
        try {
            return Files.readAllLines(target.toPath());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void writeAll(File file, String body) {
        if (file == null)
            throw new FrameworkException("Destine file is null");
        var basePath = IOUtil.target(file).basePath();

        if (file.exists() && file.isDirectory())
            throw new FrameworkException(String.format("Destine file is a directory :%s", file));

        if (!IOUtil.target(basePath).createDir().exists())
            throw new FrameworkException(String.format("Base path no exists :%s", basePath));

        if (file.exists() && !file.delete())
            throw new FrameworkException(String.format("No remove file :%s", file));

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
    }

    public static void writeLines(File file, List<String> lines) {
        final int LF = 10;
        if (file == null)
            throw new FrameworkException("Destine file is null");
        var basePath = IOUtil.target(file).basePath();

        if (file.exists() && file.isDirectory())
            throw new FrameworkException(String.format("Destine file is a directory :%s", file));

        if (!IOUtil.target(basePath).createDir().exists())
            throw new FrameworkException(String.format("Base path no exists :%s", basePath));

        if (file.exists() && !file.delete())
            throw new FrameworkException(String.format("No remove file :%s", file));

        try (FileWriter writer = new FileWriter(file)) {
            for (var line : lines) {
                writer.write(line);
                writer.write(LF);
            }
            writer.flush();
        } catch (IOException e) {
            throw new FrameworkException(e);
        }
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

    public static boolean createFileEmpty(File file) {
        try {
            if (file.exists()) {
                if (!file.canWrite())
                    throw new FrameworkException(String.format("File no accessible: %s", file.toString()));
                if (!file.delete())
                    throw new FrameworkException(String.format("File no accessible: %s", file.toString()));
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(Arrays.toString(EMPTY_BYTE_ARRAY));
            fileWriter.close();
            return file.exists();
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String toString() {
        return toString(this.getTarget());
    }

    public Path toPath() {
        return toPath(this.getTarget());
    }

    public File toFile() {
        return toFile(this.getTarget());
    }

    public boolean isEmpty() {
        return isEmpty(toString());
    }

    public String[] split() {
        return split(this.getTarget());
    }

    public boolean isFile() {
        return isFile(this.getTarget());
    }

    public boolean isDirectory() {
        return isDirectory(this.getTarget());
    }

    public boolean exists() {
        return exists(this.getTarget());
    }

    public IOUtil delete() {
        delete(this.getTarget());
        return this;
    }

    public boolean createFile() {
        return createFile(this.getTarget());
    }

    public IOUtil createDir() {
        if (!createDir(this.getTarget()))
            log.error("No create directory: {}", toString(this.getTarget()));
        return this;
    }

    public String extension() {
        return extension(this.getTarget());
    }

    public String baseName() {
        return baseName(this.getTarget());
    }

    public File basePath() {
        return basePath(this.getTarget());
    }

    public String readAll() {
        return readAll(this.toFile());
    }

    public List<String> readLines() {
        return readLines(this.toFile());
    }

    public IOUtil writeAll(String body) {
        writeAll(this.toFile(), body);
        return this;
    }

    public IOUtil writeLines(List<String> lines) {
        writeLines(this.toFile(), lines);
        return this;
    }

}
