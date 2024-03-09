package com.littlecode.files;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.util.SystemUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
public class PathUtil {
    public final List<String> pathParts = new ArrayList<>();
    @Getter
    @Setter
    public Path target;

    public static PathUtil target(Path target) {
        return PathUtil.builder().target(target).build();
    }

    public static PathUtil target(String target) {
        return PathUtil.builder().target(Path.of(target)).build();
    }

    public static PathUtil target(File target) {
        return PathUtil.builder().target(target.toPath()).build();
    }

    public static PathUtil target(URI target) {
        return PathUtil.builder().target(Path.of(target.toString())).build();
    }

    public static PathUtil targetFromTempDir() {
        return PathUtil.target(SystemUtil.Env.JAVA_TEMP_DIR);
    }

    public static PathUtil targetFromHomeDir() {
        return PathUtil.target(SystemUtil.Env.JAVA_HOME);
    }

    public static Path tempFile(UUID fileName) {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), fileName.toString());
    }

    public static Path tempFile(String fileName) {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), fileName);
    }

    public static Path tempFile(URI fileName) {
        return Path.of(fileName.toString());
    }

    public static Path tempFile() {
        return Path.of(SystemUtil.Env.JAVA_TEMP_DIR.toString(), UUID.randomUUID().toString());
    }

    public static List<File> listFiles(Path target) {
        if (target == null)
            return new ArrayList<>();
        return listFiles(target.toFile());
    }

    public static List<File> listFiles(File target) {
        if (target == null || !target.exists() || !target.isDirectory())
            return new ArrayList<>();
        File[] files = target.listFiles();

        if (files == null)
            return new ArrayList<>();
        List<File> __return = new ArrayList<>();
        for (File itemFile : files) {
            if (itemFile.isDirectory())
                __return.addAll(listFiles(itemFile.toPath()));
            else
                __return.add(itemFile.toPath().toFile());
        }
        return __return;
    }

    public static int countFiles(Path target) {
        return (target == null) ? 0 : countFiles(target.toFile());
    }

    public static int countFiles(File target) {
        if (target == null || !target.exists() || !target.isDirectory())
            return 0;
        File[] files = target.listFiles();

        if (files == null)
            return 0;

        int __return = 0;
        for (File file : files) {
            if (file.isDirectory())
                __return += countFiles(file.toPath());
            else
                ++__return;
        }
        return __return;
    }

    public static Path fileName(Path directory, Object fileName) {
        return Path.of(directory.toString(), fileName.toString());
    }

    public static boolean exists(Path target) {
        return target.toFile().exists();
    }

    public static void mkDir(Path target) {
        if (target == null)
            return;
        mkDir(target.toFile());

    }

    public static void mkDir(File target) {
        if (target == null)
            return;
        if (target.exists()) {
            if (target.isFile())
                throw new FrameworkException("file is a directory: " + target);
            return;
        }

        if (!target.mkdirs())
            throw new FrameworkException("No create paths: " + target);
    }

    public static void rmDir(Path path) {
        if (path == null)
            return;
        rmDir(path.toFile());
    }

    public static void rmDir(File file) {
        if (file == null)
            return;
        if (!file.exists())
            return;
        if (!file.delete())
            throw new FrameworkException("No delete file: " + file);
//        var list=listFiles(directory);
//        list
//                .forEach(file ->{
//                            if(!file.delete())
//                                throw new FrameworkException("No delete file: "+ file.toString());
//                        }
//
//                );
    }

    public PathUtil clear() {
        this.target = null;
        return this.clean();
    }

    public PathUtil clean() {
        this.pathParts.clear();
        return this;
    }

    public PathUtil part(String part) {
        if (part != null)
            this.pathParts.add(part);
        return this;
    }

    public PathUtil part(UUID part) {
        if (part != null)
            this.part(part.toString());
        return this;
    }

    public Path toPath() {
        var path = new StringBuilder(this.target == null ? "" : this.target.toString());
        this.pathParts
                .forEach(s -> {
                    if (s == null)
                        return;
                    s = s.trim();
                    if (!s.isEmpty())
                        path.append(IOUtils.DIR_SEPARATOR).append(s);
                });
        var finalPath = path.toString().trim();
        return Path.of(finalPath);
    }

    public String toString() {
        var path = this.toPath();
        return (path == null) ? "" : path.toString();
    }

    public File toFile() {
        var path = this.toPath();
        return path == null ? null : path.toFile();
    }

    public List<File> listFiles() {
        return listFiles(this.toFile());
    }

    public Integer countFiles() {
        var file = this.toFile();
        return file == null ? 0 : countFiles(file);
    }

    public Path fileName(Object fileName) {
        var file = this.toFile();
        return file == null ? null : Path.of(this.toFile().toString(), fileName.toString());
    }

    public boolean exists() {
        return exists(target);
    }

    public PathUtil mkDir() {
        mkDir(this.toPath());
        return this;
    }

    public PathUtil rmDir() {
        rmDir(this.toFile());
        return this;
    }

}
